import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import ConfirmationModal from "../components/ConfirmationModal.jsx";

function UserProblems() {
    const [userProblems, setUserProblems] = useState([]);
    const [searchId, setSearchId] = useState("");
    const [searchResult, setSearchResult] = useState([]);
    const [selectedProblem, setSelectedProblem] = useState(null);
    const [isRevising, setIsRevising] = useState(false);

    async function fetchUserProblems() {
        const token = localStorage.getItem("token");

        try {
            const response = await fetch(
                "http://localhost:8080/api/users/me/problems",
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            if (!response.ok) {
                throw new Error("Failed to fetch user problems");
            }

            const data = await response.json();
            setUserProblems(data);
        } catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        fetchUserProblems();
    }, []);

    // Search handler
    const handleSearch = (value) => {
        const query = value.trim();

        if (!query) {
            setSearchResult([]);
            return;
        }

        // Search by exact problem ID when input is numeric
        if (/^\d+$/.test(query)) {
            const result = userProblems.find(
                (problem) => Number(problem.problemId) === Number(query)
            );

            setSearchResult(result ? [result] : []);
            return;
        }

        // Search by problem title
        const results = userProblems.filter((problem) =>
            problem.title?.toLowerCase().includes(query.toLowerCase())
        );

        setSearchResult(results);
    };

    async function handleRevise() {
        if (!selectedProblem) return;

        const token = localStorage.getItem("token");
        setIsRevising(true);

        try {
            const response = await fetch(
                `http://localhost:8080/api/problems/${selectedProblem.problemId}/revise`,
                {
                    method: "PUT",
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            if (!response.ok) {
                throw new Error("Failed to revise problem");
            }

            await fetchUserProblems();
            setSelectedProblem(null);
        } catch (error) {
            console.error(error);
        } finally {
            setIsRevising(false);
        }
    }

    return (
        <div className="user-problems-page">
            <div className="search-container">
                <img
                    src="/searchIcon.svg"
                    alt="Search"
                    className="search-icon"
                />

                <input
                    type="text"
                    value={searchId}
                    onChange={(e) => {
                        const value = e.target.value;
                        setSearchId(value);
                        handleSearch(value);
                    }}
                    placeholder="Search by problem ID or title"
                />
            </div>

            <hr className={"card-divider"}/>

            <div className="user-problem-list">
                {(searchId.trim() ? searchResult : userProblems).map(
                    (problem) => (
                        <div
                            className="user-problem-card"
                            key={problem.problemId}
                        >
                            <div className="user-problem-content">
                                <div className="user-problem-heading">
                                    <div className="user-problem-title-group">
                                        <span className="user-problem-pill user-problem-id-pill">
                                            {problem.problemId}
                                        </span>
                                        <Link
                                            className="user-problem-link user-problem-pill user-problem-title-pill"
                                            target={"_blank"}
                                            rel={"noopener noreferrer"}
                                            to={problem.url}
                                        >
                                            <h2>{problem.title}</h2>
                                        </Link>
                                    </div>
                                    <span className={`user-problem-pill user-problem-difficulty difficulty-${problem.difficulty?.toLowerCase()}`}>
                                        {problem.difficulty}
                                    </span>
                                </div>

                                <div className="user-problem-pills">
                                    <span className="user-problem-pill user-problem-solved-date">
                                        <span>Solved date</span>
                                        <span className="user-problem-pill-value">
                                            {problem.solved_date}
                                        </span>
                                    </span>
                                    <span className="user-problem-pill user-problem-revision-count">
                                        <span>Times revised</span>
                                        <span className="user-problem-pill-value">
                                            {problem.revision_count}
                                        </span>
                                    </span>
                                    <span className="user-problem-pill user-problem-next-revision">
                                        <span>Next revision date</span>
                                        <span className="user-problem-pill-value">
                                            {problem.next_revision_date}
                                        </span>
                                    </span>
                                </div>
                            </div>

                            <button
                                className="revise-button"
                                onClick={() => setSelectedProblem(problem)}
                            >
                                Revise
                            </button>
                        </div>
                    )
                )}
            </div>

            {selectedProblem && (
                <ConfirmationModal
                    title="Revise Problem?"
                    message={<>Are you sure you want to revise <strong>{selectedProblem.title}</strong>?</>}
                    isBusy={isRevising}
                    onCancel={() => setSelectedProblem(null)}
                    onConfirm={handleRevise}
                    confirmText="Revise"
                    busyText="Revising..."
                />
            )}
        </div>
    );
}

export default UserProblems;
