import {use, useEffect, useState} from "react";
import { Link } from "react-router-dom";

function Problems() {

    const [problems, setProblems] = useState([]);
    const [searchId, setSearchId] = useState("");
    const [searchResult, setSearchResult] = useState(null);

    useEffect(() => {

        const params = new URLSearchParams(window.location.search);
        const token = params.get("token");

        if (token) {
            localStorage.setItem("token", token);
        }

        const savedToken = localStorage.getItem("token");

        fetch("http://localhost:8080/api/problems", {
            headers: {
                Authorization: `Bearer ${savedToken}`
            }
        })
            .then(response => response.json())
            .then(data => setProblems(data));

    }, []);

    //search handler
    const handleSearch = () => {
        const target = Number(searchId)

        let left = 0;
        let right = problems.length - 1;

        while(left <= right) {
            const mid = Math.floor((left + right) / 2);
            const id = problems[mid].problemId;

            if(id === target) {
                setSearchResult(problems[mid]);
                return;
            }

            if(id < target) {
                left = mid + 1;
            }else {
                right = mid - 1;
            }
        }
        setSearchResult(null)
    }


    return (
        <main className="problems-page">
            <h1>Problems</h1>

            <div className={"search-container"}>
                <input
                    type="number"
                    value={searchId}
                    onChange={(e) =>
                        setSearchId(e.target.value)}
                    placeholder="Search by problem ID"
                />

                <button onClick={handleSearch}>
                    Search
                </button>
            </div>

            <div className="problem-list">
                {searchResult ? (
                    <Link
                        to={`/problems/${searchResult.problemId}`}
                        className="problem-card"
                        key={searchResult.problemId}
                    >
                        <span className="problem-title">
                            {searchResult.problemId} : {searchResult.problem_title}
                        </span>

                        <span className="problem-difficulty">
                            {searchResult.problem_diff}
                        </span>
                    </Link>
                ) : (
                    problems.map(problem => (
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