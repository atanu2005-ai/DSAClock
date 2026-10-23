import {use, useEffect, useState} from "react";
import { Link } from "react-router-dom";

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

        try {
            fetch("http://localhost:8080/api/problems", {
                headers: {
                    Authorization: `Bearer ${savedToken}`
                }
            })
                .then(response => response.json())
                .then(data => setProblems(data));
        }catch (error) {
            console.log("failed to fetch problems");
        }finally {
            setLoading(false);
        }

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
                {(searchId.trim() ? searchResult : problems).map(problem => (
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
                ))}
            </div>
        </main>
    );
}

export default Problems;