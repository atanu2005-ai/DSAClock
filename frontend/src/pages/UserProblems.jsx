import { useState, useEffect } from "react";
import { Link } from "react-router-dom";

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
            <h1>My Problems</h1>

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
                        <div
                            className="user-problem-card"
                            key={problem.problemId}
                        >
                            <Link
                                className="user-problem-link"
                                target={"_blank"}
                                rel={"noopener noreferrer"}
                                to={problem.url}
                            >
                                <h2>
                                    {problem.problemId} : {problem.title}
                                </h2>
                            </Link>

                            <span>{problem.difficulty}</span>

                            <p>Solved date: {problem.solved_date}</p>
                            <p>
                                Next revision date: {problem.next_revision_date}
                            </p>

                            <button
                                className="revise-button"
                                onClick={() => handleRevise(problem.problemId)}
                            >
                                Revise
                            </button>

                            <p>Times revised: {problem.revision_count}</p>
                        </div>
                    )
                )}
            </div>
        </div>
    );
}

export default UserProblems;