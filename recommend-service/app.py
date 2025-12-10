from flask import Flask, jsonify, request
import requests
from sentence_transformers import SentenceTransformer
import numpy as np
from numpy import dot
from numpy.linalg import norm
import threading
import time

app = Flask(__name__)

# URL của product-service
PRODUCT_SERVICE_URL = "http://product-service:8080/products"
# Khoảng thời gian để cập nhật embedding
POLL_INTERVAL = 60  # giây
# Khởi tạo mô hình embedding
model = SentenceTransformer('all-MiniLM-L6-v2')
# Lưu trữ embeddings của sản phẩm
embeddings = {}
products = []
product_dict = {}

# Endpoint kiểm tra health của service
@app.route("/recommend/health")
def health():
    return {"status": "ok"}, 200

# Hàm để lấy gợi ý sản phẩm tương tự
@app.route("/recommend", methods=["POST"])
def recommend():
    data = request.get_json() or {}
    product_id = data.get("productId")

    if not product_id:
        return jsonify({"error": "productId is required"}), 400

    # trả về các sản phẩm tương tự
    recommendations_ids = get_similar_products(product_id, embeddings, top_n=10)
    
    return jsonify({
        "productId": product_id,
        "recommendations": [product_dict[pid] for pid in recommendations_ids if pid in product_dict]
    })

# Hàm lấy tất cả sản phẩm từ product-service
def fetch_all_products():
    try:
        response = requests.get(PRODUCT_SERVICE_URL, timeout=5)
        response.raise_for_status()
        data = response.json()
        return data
    except Exception as e:
        print("Error fetching products:", e)
        return []

# Hàm tính embedding cho tất cả sản phẩm    
def compute_product_embeddings(products):
    """
    products: list dict, mỗi dict chứa info sản phẩm
    trả về: dict {productId: embedding vector}
    """
    embeddings = {}
    for p in products:
        text = (p.get("name", "") + " " + p.get("description", "")).strip()
        if text:
            vec = model.encode(text)
            embeddings[p["id"]] = vec
    return embeddings

# Hàm tính cosine similarity
def cosine_similarity(vec1, vec2):
    return dot(vec1, vec2) / (norm(vec1) * norm(vec2))

# Hàm lấy top-N sản phẩm tương tự
def get_similar_products(product_id, embeddings, top_n=10):
    if product_id not in embeddings:
        return []

    target_vec = embeddings[product_id]
    scores = []

    for pid, vec in embeddings.items():
        if pid == product_id:
            continue
        score = cosine_similarity(target_vec, vec)
        scores.append((pid, score))

    # Sắp xếp theo score giảm dần
    scores.sort(key=lambda x: x[1], reverse=True)
    top_products = [pid for pid, _ in scores[:top_n]]
    return top_products

# Thread để cập nhật embedding định kỳ
def update_embeddings_periodically():
    global embeddings, products, product_dict
    while True:
        print("Starting update embeddings ...", flush=True)
        new_products = fetch_all_products()
        new_embeddings = compute_product_embeddings(new_products)

        if new_embeddings:
            embeddings = new_embeddings
            products = new_products
            product_dict = {p["id"]: p for p in products}
            print(f"Updated embeddings: {len(embeddings)} products", flush=True)
        else:
            print("Update failed, keeping old embeddings", flush=True)
        time.sleep(POLL_INTERVAL)

# Khởi tạo embeddings ban đầu
embeddings = compute_product_embeddings(fetch_all_products())

# Bắt đầu thread cập nhật embeddings
threading.Thread(target=update_embeddings_periodically, daemon=True).start()


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)