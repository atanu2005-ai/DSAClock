import {useEffect, useState} from "react";
import { Link } from "react-router-dom";
import ProblemCardSkeleton from "./ProblemCardSkeleton.jsx";

function Problems() {

    const [problems, setProblems] = useState([]);
    const [searchId, setSearchId] = useState("");
    const [searchResult, setSearchResult] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const token = params.get("token");

        if (token) {
            localStorage.setItem("token", token);
        }

        const savedToken = localStorage.getItem("token");

        async function fetchProblems() {
            try {
                const response = await fetch(
                    "http://localhost:8080/api/problems",
                    {
                        headers: {
                            Authorization: `Bearer ${savedToken}`
                        }
                    }
                );

                if (!response.ok) {
                    throw new Error("Failed to fetch problems");
                }

                const data = await response.json();
                setProblems(data);
            } catch (error) {
                console.error("Failed to fetch problems:", error);
            } finally {
                setLoading(false);
            }
        }

        fetchProblems();
    }, []);

    //search handler
    const handleSearch = (value = searchId) => {
        const query = value.trim();

        if (!query) {
            setSearchResult([]);
            return;
        }

        // Search by problem ID when input is numeric
        if (/^\d+$/.test(query)) {
            const target = Number(query);

            const result = problems.find(
                (problem) => Number(problem.problemId) === target
            );

            setSearchResult(result ? [result] : []);
            return;
        }

        // Search by problem title when input contains text
        const results = problems.filter((problem) =>
            problem.problem_title
                ?.toLowerCase()
                .includes(query.toLowerCase())
        );

        setSearchResult(results);
    };


    return (
        <main className="problems-page">
            <div className={"search-container"}>
                <img src={"/searchIcon.svg"} alt={"search"} className={"search-icon"}/>
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

                <div className="problem-list">
                    {loading ? (
                        Array.from({ length: 10 }).map((_, index) => (
                            <ProblemCardSkeleton key={index} />
                        ))
                        ) : (
                        (searchId.trim() ? searchResult : problems)?.map((problem) => (
                            <Link
                                to={`/problems/${problem.problemId}`}
                                className="problem-card"
                                key={problem.problemId}
                            >
                                <span className="problem-title">
                                    {problem.problemId} : {problem.problem_title}
                                </span>

                                <span className="problem-difficulty">
                                    {problem.problem_diff}
                                </span>
                            </Link>
                        ))
                    )}
                </div>
        </main>
    );
}

export default Problems;