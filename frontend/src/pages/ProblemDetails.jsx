import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";

function ProblemDetails() {

    const {problemId} = useParams()
    const [problem, setProblem] = useState(null)

    useEffect(() => {
        fetch(`http://localhost:8080/api/problems/${problemId}`)
            .then(response => response.json())
            .then(data => setProblem(data))
    }, [problemId])

    return (
        <main className={"problem-details"}>
            <h1>
                {problemId} : {problem?.problem_title}
            </h1>

            <button>Add</button>
        </main>
    )
}

export default ProblemDetails