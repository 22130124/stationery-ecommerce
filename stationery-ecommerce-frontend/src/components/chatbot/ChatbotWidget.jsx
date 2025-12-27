// ChatbotWidget.jsx
import {useRef, useState} from 'react'
import styles from './ChatbotWidget.module.scss'
import {processChatRequest} from '../../api/aiApi'
import ProductCard from "../../features/product-list/components/ProductCard";
import {FaRegCommentDots, FaRobot} from "react-icons/fa";
import ReactTypingEffect from 'react-typing-effect';
import {IoClose} from "react-icons/io5";

export default function ChatbotWidget() {
    const [isOpen, setIsOpen] = useState(false)
    const [input, setInput] = useState('')
    const [chatHistory, setChatHistory] = useState([])
    const [relatedChatHistory, setRelatedChatHistory] = useState([])
    const [botTyping, setBotTyping] = useState(false)
    const messagesEndRef = useRef(null)
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
            setChatHistory(prev => [...prev, {from: "bot", text: "Có lỗi xảy ra, vui lòng thử lại sau"}]);
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

        setTimeout(scrollToBottom, 0);
    }

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };


    return (
        <>
            {!isOpen && (
                <div className={styles.chatButton} onClick={handleOpenChat}>
                    <FaRegCommentDots size={32}/>
                    <div className={styles.pulse}></div>
                </div>
            )}

            {isOpen && (
                <div className={styles.chatWindow}>
                    <div className={styles.header}>
                        <div className={styles.headerInfo}>
                            <div className={styles.botAvatar}>
                                <FaRobot/>
                            </div>
                            <div className={styles.botInfoText}>
                                <div className={styles.botName}>Trợ lý AI</div>
                                <div className={styles.botStatus}>
                                    <span className={styles.onlineDot}></span>
                                    Đang online
                                </div>
                            </div>
                        </div>
                        <button onClick={() => setIsOpen(false)} className={styles.closeBtn}>
                            <IoClose size={26}/>
                        </button>
                    </div>

                    <div className={styles.messages}>
                        {chatHistory.map((message, index) => (
                            <div
                                key={index}
                                className={`${styles.message} ${styles[message.from]} ${styles.fadeIn}`}
                            >
                                {message.from === 'bot' && (
                                    <div className={styles.botAvatarSmall}>
                                        <FaRobot/>
                                    </div>
                                )}
                                <div className={styles.bubbleWrapper}>
                                    <div className={styles.bubble}>{message.text}</div>
                                    {message.products?.length > 0 && (
                                        <div className={styles.productSection}>
                                            <div className={styles.productList}>
                                                {message.products.map((p) => (
                                                    <div key={p.id} className={styles.productCardWrapper}>
                                                        <ProductCard product={p} />
                                                    </div>
                                                ))}
                                            </div>
                                        </div>
                                    )}
                                </div>
                            </div>
                        ))}

                        {botTyping && (
                            <div className={`${styles.message} ${styles.bot}`}>
                                <div className={styles.botAvatarSmall}>
                                    <FaRobot/>
                                </div>
                                <div className={styles.bubbleWrapper}>
                                    <div className={styles.typingIndicator}>
                                        <span></span>
                                        <span></span>
                                        <span></span>
                                    </div>
                                </div>
                            </div>
                        )}
                        <div ref={messagesEndRef}/>
                    </div>

                    <div className={styles.inputArea}>
                        <input
                            type='text'
                            value={input}
                            placeholder='Nhập tin nhắn...'
                            onChange={(e) => setInput(e.target.value)}
                            onKeyDown={(e) => e.key === 'Enter' && !e.shiftKey && handleSend()}
                        />
                        <button onClick={handleSend}>Gửi</button>
                    </div>
                </div>
            )}
        </>
    )
}
