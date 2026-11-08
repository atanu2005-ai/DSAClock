import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "./UserProblems.css";

function formatTimelineDate(date) {
    if (!date) return "—";

    const [year, month, day] = date.split("-");
    return new Date(Number(year), Number(month) - 1, Number(day)).toLocaleDateString(
        "en-GB",
        { day: "2-digit", month: "short", year: "numeric" }
    );
}

function UserProblems() {
    const [userProblems, setUserProblems] = useState([]);
    const [searchId, setSearchId] = useState("");
    const [searchResult, setSearchResult] = useState([]);

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
        if (/^\\d+$/.test(query)) {
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

    async function handleRevise(problemId) {
        const token = localStorage.getItem("token");

        try {
            const response = await fetch(
                `http://localhost:8080/api/problems/${problemId}/revise`,
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
        } catch (error) {
            console.error(error);
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

            <div className="user-problem-list">
                {(searchId.trim() ? searchResult : userProblems).map(
                    (problem) => (
                        <article
                            className="user-problem-card"
                            key={problem.problemId}
                        >
                            <div className="user-problem-content">
                                <div className="user-problem-heading">
                                    <Link
                                        className="user-problem-link"
                                        target="_blank"
                                        rel="noopener noreferrer"
                                        to={problem.url}
                                    >
                                        <h2>
                                            {problem.problemId} : {problem.title}
                                        </h2>
                                    </Link>

                                    <span className="user-problem-difficulty">
                                        {problem.difficulty}
                                    </span>
                                </div>

                                <div className="user-problem-timeline">
                                    <div className="timeline-rail" aria-hidden="true">
                                        <span className="timeline-dot timeline-dot-solved" />
                                        <span className="timeline-stem" />
                                        <span className="timeline-dot timeline-dot-next" />
                                    </div>

                                    <div className="timeline-details">
                                        <div className="timeline-event">
                                            <span className="timeline-label">Solved</span>
                                            <time>{formatTimelineDate(problem.solved_date)}</time>
                                        </div>

                                        <div className="timeline-event">
                                            <span className="timeline-label">Next revision</span>
                                            <time>{formatTimelineDate(problem.next_revision_date)}</time>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className="user-problem-actions">
                                <span className="user-problem-revision-count">
                                    {problem.revision_count} {problem.revision_count === 1 ? "revision" : "revisions"}
                                </span>

                                <button
                                    className="revise-button"
                                    onClick={() => handleRevise(problem.problemId)}
                                >
                                    Revise
                                </button>
                            </div>
                        </article>
                    )
                )}
            </div>
        </div>
    );
}

export default UserProblems;