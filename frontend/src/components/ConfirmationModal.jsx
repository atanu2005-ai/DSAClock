function ConfirmationModal({
    title,
    message,
    isBusy = false,
    onCancel,
    onConfirm,
    confirmText = "Confirm",
    busyText = "Working..."
}) {
    return (
        <div
            className="modal-overlay"
            onClick={() => !isBusy && onCancel()}
        >
            <div
                className="add-problem-modal"
                role="dialog"
                aria-modal="true"
                aria-labelledby="confirmation-modal-title"
                onClick={(event) => event.stopPropagation()}
            >
                <h2 id="confirmation-modal-title">{title}</h2>
                <p>{message}</p>

                <div className="add-problem-actions">
                    <button
                        className="cancel-add"
                        onClick={onCancel}
                        disabled={isBusy}
                    >
                        Cancel
                    </button>
                    <button
                        className="confirm-add"
                        onClick={onConfirm}
                        disabled={isBusy}
                    >
                        {isBusy ? busyText : confirmText}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default ConfirmationModal;
