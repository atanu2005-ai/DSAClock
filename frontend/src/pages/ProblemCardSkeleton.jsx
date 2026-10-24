function ProblemCardSkeleton() {
    return (
        <div className="problem-card skeleton-card">
            <div className="skeleton skeleton-icon"></div>

            <div className="skeleton-content">
                <div className="skeleton skeleton-title"></div>
                <div className="skeleton skeleton-subtitle"></div>

            </div>

            <div className={"skeleton-tags"}>
                <div className="skeleton skeleton-tag"></div>
            </div>
        </div>
    );
}

export default ProblemCardSkeleton;