import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

function ProblemDetails() {

    const { problemId } = useParams();
    const navigate = useNavigate();

    const [problem, setProblem] = useState(null);
    const [description, setDescription] = useState("");
    const [showConfirm, setShowConfirm] = useState(false);
    const [isAdding, setIsAdding] = useState(false);

    useEffect(() => {
        fetch(`http://localhost:8080/api/problems/${problemId}`)
            .then(response => response.json())
            .then(data => setProblem(data));
    }, [problemId]);
    console.log(problem?.problem_diff);

    useEffect(() => {
        fetch(`http://localhost:8080/api/problems/${problemId}/details`)
            .then(response => response.text())
            .then(data => setDescription(data));
    }, [problemId]);

    // Add problem to user's collection
    const handleAdd = async () => {
        setIsAdding(true);

        try {
            const response = await fetch(
                `http://localhost:8080/api/problems/${problemId}/add`,
                {
                    method: "POST",
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("token")}`
                    }
                }
            );

            if (response.ok) {
                navigate("/my-problems");
                return;
            }

            if (response.status === 409) {
                alert("Problem already added");
                setShowConfirm(false);
                return;
            }

            throw new Error("Failed to add problem");

        } catch (error) {
            console.error("Failed to add problem:", error);
            alert("Something went wrong. Please try again.");
        } finally {
            setIsAdding(false);
        }
    };

    return (
        <main className="problem-details">

            <div className={"problem-details-card"}>
                <div className={"problem-details-header"}>
                    <h1>{problem?.problem_title}</h1>

                    <a
                        href={problem?.problem_url}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="problem-link"
                    >
                        <img src={"/LeetcodeIcon.svg"} alt={"leetcode"}/>
                    </a>

                    <p className="problem-details-difficulty">
                        {problem?.problem_diff}
                    </p>

                </div>

                <hr className={"card-divider"}/>

                <div
                    className="problem-description"
                    dangerouslySetInnerHTML={{ __html: description }}
                />

                <div className="problem-stats">
                    <span className="problem-likes">
                        <img src="/likelogo.svg" alt="likes" />
                        {problem?.problem_likes}
                    </span>

                    <span className="problem-dislikes">
                        <img src="/dislikelogo.svg" alt="dislikes" />
                        {problem?.problem_dislikes}
                    </span>
                </div>

                <button
                    className="problem-details-buttons"
                    onClick={() => setShowConfirm(true)}
                >
                    Add
                </button>
            </div>

            {/* Confirmation Modal */}
            {showConfirm && (
                <div
                    className="modal-overlay"
                    onClick={() => !isAdding && setShowConfirm(false)}
                >
                    <div
                        className="add-problem-modal"
                        role="dialog"
                        aria-modal="true"
                        aria-labelledby="add-problem-title"
                        onClick={e => e.stopPropagation()}
                    >
                        <h2 id="add-problem-title">Add Problem?</h2>

                        <p>
                            Are you sure you want to add
                            <strong> {problem?.problem_title} </strong>
                            to your collection?
                        </p>

                        <div className="add-problem-actions">
                            <button
                                className="cancel-add"
                                onClick={() => setShowConfirm(false)}
                                disabled={isAdding}
                            >
                                Cancel
                            </button>

                            <button
                                className="confirm-add"
                                onClick={handleAdd}
                                disabled={isAdding}
                            >
                                {isAdding ? "Adding..." : "Confirm"}
                            </button>
                        </div>
                    </div>
                </div>
            )}

        </main>
    );
}

export default ProblemDetails;