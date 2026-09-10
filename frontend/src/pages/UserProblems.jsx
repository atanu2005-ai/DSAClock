import { useState, useEffect } from "react";
import {Link} from "react-router-dom";

function UserProblems() {

    const [userProblems, setUserProblems] = useState([]);

    async function fetchUserProblems() {
        const token = localStorage.getItem("token");

        const response = await fetch(
            "http://localhost:8080/api/users/me/problems",
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        );

        const data = await response.json();
        setUserProblems(data);
    }

    useEffect(() => {
        fetchUserProblems();
    }, []);

    async function handleRevise(problemId) {
        const token = localStorage.getItem('token');

        try {
            const response = await fetch(`http://localhost:8080/api/problems/${problemId}/revise`, {
                method: "PUT",
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            })

            if(!response.ok) {
                throw new Error("Failed to revise problem");
            }

            // Update the userProblems state to reflect the changes
            await fetchUserProblems()

        }catch (error) {
            console.error(error);
        }
    }

    return (
        <div className="user-problems-page">
            <h1>My Problems</h1>

            <div className="user-problem-list">
                {userProblems.map(problem => (
                    <div className="user-problem-card" key={problem.problemId}>
                        <h2>{problem.title}</h2>
                        <span>{problem.difficulty}</span>
                        <p>Solved date: {problem.solved_date}</p>
                        <p>Next revision date: {problem.next_revision_date}</p>

                        <button className={"revise-button"}
                                onClick={() => handleRevise(problem.problemId)}>
                            Revise
                        </button>

                        <p>Times revised: {problem.revision_count}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default UserProblems;