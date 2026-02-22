// Hàm chuyển đổi từ tên danh mục thành slug tương ứng
export const slugify = (text) => {
    return text
        .toLowerCase()
        .normalize("NFD") // tách dấu khỏi chữ
        .replace(/[\u0300-\u036f]/g, "") // xoá dấu
        .replace(/đ/g, "d") // xử lý riêng chữ đ
        .replace(/[^a-z0-9\s-]/g, "") // bỏ ký tự đặc biệt
        .trim()
        .replace(/\s+/g, "-") // space -> -
        .replace(/-+/g, "-"); // gộp nhiều - thành 1
};