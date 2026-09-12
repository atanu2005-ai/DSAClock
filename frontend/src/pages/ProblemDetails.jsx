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

            <div className={"problem-description"}
                 dangerouslySetInnerHTML={{__html:description}} />

            <button className={"problem-details-buttons"} onClick={handleAdd}>
                Add
            </button>

        </main>
    )
}

export default ProblemDetails