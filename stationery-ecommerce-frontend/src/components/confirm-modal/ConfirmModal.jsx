import { useEffect } from "react";
import "./ConfirmModal.css";

const ConfirmModal = ({
                          open,
                          title = "Xác nhận",
                          message,
                          confirmText = "Xác nhận",
                          cancelText = "Hủy",
                          onConfirm,
                          onCancel,
                          danger = false,
                      }) => {
    useEffect(() => {
        const handleEsc = (e) => {
            if (e.key === "Escape") onCancel();
        };
        if (open) window.addEventListener("keydown", handleEsc);
        return () => window.removeEventListener("keydown", handleEsc);
    }, [open, onCancel]);

    if (!open) return null;

    return (
        <div className="cm-overlay" onClick={onCancel}>
            <div className="cm-modal" onClick={(e) => e.stopPropagation()}>
                <h3 className="cm-title">{title}</h3>

                {message && <p className="cm-message">{message}</p>}

                <div className="cm-actions">
                    <button className="cm-btn ghost" onClick={onCancel}>
                        {cancelText}
                    </button>

                    <button
                        className={`cm-btn primary ${danger ? "danger" : ""}`}
                        onClick={onConfirm}
                    >
                        {confirmText}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ConfirmModal;
