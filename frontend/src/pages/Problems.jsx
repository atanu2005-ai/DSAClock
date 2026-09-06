import {useEffect, useState} from "react";
import {Link} from "react-router-dom";

function Problems() {

    const [problems, setProblems] = useState([])

    useEffect(() => {
        fetch('http://localhost:8080/api/problems')
            .then(response => response.json())
            .then(date => setProblems(date))
    }, [])
    return (
        <main className="problems-page">
            <h1>Problems</h1>

            <div className="problem-list">
                {problems.map(problem => (
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
    )
}

export default Problems