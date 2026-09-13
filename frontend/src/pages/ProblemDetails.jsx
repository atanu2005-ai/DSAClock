import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

function ProblemDetails() {

    const {problemId} = useParams()
    const [problem, setProblem] = useState(null)
    const navigate = useNavigate()

    useEffect(() => {
        fetch(`http://localhost:8080/api/problems/${problemId}`)
            .then(response => response.json())
            .then(data => setProblem(data))
    }, [problemId])

    //for thr problem description
    const [description, setDescription] = useState("")

    useEffect(() => {
        fetch(`http://localhost:8080/api/problems/${problemId}/details`)
            .then(response => response.text())
            .then(data => setDescription(data))
    }, [problemId])

    //for handling add button
    const handleAdd = () => {
        fetch(`http://localhost:8080/api/problems/${problemId}/add`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            }
        })
            .then(async response => {
                if(response.ok) {
                    navigate("/my-problems");
                    return;
                }

                if(response.status === 409) {
                    alert("problem already added")
                    return;
                }

                throw new Error("Failed to add problem")
            })
    }

    return (
        <main className={"problem-details"}>

            <h1>{problem?.problem_title}</h1>
            <p className={"problemdetails-difficulty"}>
                {problem?.problem_diff}
            </p>

            <a href={problem?.problem_url}
               target={"_blank"}
               rel={"noopener noreferrer"}
               className={"problem-link"}>

                View on leetcode
            </a>

            <div className={"problem-description"}
                 dangerouslySetInnerHTML={{__html:description}} />

            <div className={"problem-stats"}>
                <span className={"problem-likes"}>

                    <img src="/likelogo.svg" alt={"likes"} />
                   {problem?.problem_likes}
                </span>
                <span className={"problem-dislikes"}>
                    <img src="/dislikelogo.svg" alt={"dislikes"}/>
                   {problem?.problem_dislikes}
                </span>
            </div>

            <button className={"problem-details-buttons"} onClick={handleAdd}>
                Add
            </button>

        </main>
    )
}

export default ProblemDetails