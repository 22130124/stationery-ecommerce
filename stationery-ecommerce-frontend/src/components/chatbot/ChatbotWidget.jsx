import {useState} from 'react'
import styles from './ChatbotWidget.module.scss'
import {processChatRequest} from '../../api/aiApi'
import ProductCard from "../../features/product-list/components/ProductCard";
import { FaRegCommentDots } from "react-icons/fa";
import ReactTypingEffect from 'react-typing-effect';

export default function ChatbotWidget() {
    const [isOpen, setIsOpen] = useState(false)
    const [input, setInput] = useState('')
    const [chatHistory, setChatHistory] = useState([])
    const [relatedChatHistory, setRelatedChatHistory] = useState([])
    const [botTyping, setBotTyping] = useState(false)
    const defaultBotMessage = {
        from: 'bot',
        text: 'Xin chào, bạn muốn tìm kiếm sản phẩm gì hôm nay?',
        products: [],
        related: false
    };
    const [hasShownGreeting, setHasShownGreeting] = useState(false)

    const handleSend = async () => {
        // Nếu là chuỗi rỗng thì không gửi
        if (!input.trim()) return

        const userMessage = {
            from: 'user',
            text: input,
        }

        setChatHistory((prev) => [...prev, userMessage])
        setInput('')

        setBotTyping(true)

        try {
            const body = {
                messages: relatedChatHistory.length > 0
                    ? [...relatedChatHistory, userMessage.text]
                    : [userMessage.text]
            }

            const res = await processChatRequest(body)

            const botMessage = {
                from: 'bot',
                text: res.message,
                products: res.products || [],
                related: res.related,
            }

            // Nếu như tin nhắn vừa rồi không liên quan đên các tin nhắn trước đó thì reset lại relatedChatHistory
            if (!botMessage.related) {
                setRelatedChatHistory([userMessage.text, botMessage.text])
            } else {
                setRelatedChatHistory((prev) => [...prev, userMessage.text, botMessage.text])
            }

            setChatHistory(prev => [...prev, botMessage])
        } catch (error) {
            setChatHistory(prev => [...prev, { from: "bot", text: "Có lỗi xảy ra, vui lòng thử lại sau" }]);
        } finally {
            setBotTyping(false)
        }
    }

    const handleOpenChat = () => {
        setIsOpen(true)
        if (!hasShownGreeting) {
            setChatHistory(prev => [
                ...prev,
                {
                    from: 'bot',
                    text: 'Xin chào, bạn muốn tìm kiếm sản phẩm gì hôm nay?',
                    products: [],
                    related: false
                }
            ]);
            setHasShownGreeting(true);
        }
    }

    return (
        <>
            {/* Nút thu nhỏ */}
            {!isOpen && (
                <div className={styles.chatButton} onClick={handleOpenChat}>
                    <FaRegCommentDots size={28} />
                </div>
            )}

            {/* Cửa sổ chat */}
            {isOpen && (
                <div className={styles.chatWindow}>
                    <div className={styles.header}>
                        <span>AI hỗ trợ</span>
                        <button onClick={() => setIsOpen(false)}>x</button>
                    </div>

                    <div className={styles.messages}>
                        {chatHistory.map((message, index) => (
                            <div
                                key={index}
                                className={`${styles.message} ${
                                    message.from === 'user' ? styles.user : styles.bot
                                }`}
                            >
                                <div className={styles.bubble}>{message.text}</div>

                                {/* Nếu có sản phẩm */}
                                {message.products?.length > 0 && (
                                    <div className={styles.productList}>
                                        {message.products.map((p) => (
                                            <ProductCard key={p.id} product={p} />
                                        ))}
                                    </div>
                                )}
                            </div>
                        ))}
                        {botTyping && (
                            <div className={`${styles.message} ${styles.bot}`}>
                                <div className={styles.bubble}>
                                    <ReactTypingEffect
                                        text={["..."]}
                                        speed={100}
                                        eraseDelay={1000}
                                        displayTextRenderer={(text, i) => <span>{text}</span>}
                                    />
                                </div>
                            </div>
                        )}
                    </div>

                    <div className={styles.inputArea}>
                        <input
                            type='text'
                            value={input}
                            placeholder='Hỏi gì cũng được...'
                            onChange={(e) => setInput(e.target.value)}
                            onKeyDown={(e) => e.key === 'Enter' && handleSend()}
                        />
                        <button onClick={handleSend}>Gửi</button>
                    </div>
                </div>
            )}
        </>
    )
}
