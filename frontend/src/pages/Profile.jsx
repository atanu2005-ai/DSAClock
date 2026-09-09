import {Link} from "react-router-dom";
import {useState, useEffect} from "react";
import { useNavigate } from "react-router-dom";

function Profile() {
    const [user, setUser] = useState(null);
    const navigate = useNavigate();

    function handleLogout() {
        localStorage.removeItem('token');
        window.dispatchEvent(new Event("authChange")); // Dispatch a storage event to notify other tabs
        navigate("/login")
    }

    useEffect(() => {

        const token = localStorage.getItem('token');
        console.log(token);
        if (token) {
            fetch('http://localhost:8080/api/users/me', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
            .then(response => response.json())
            .then(data => {setUser(data)})
            .catch(error => console.error('Error fetching user profile:', error));
        }

    }, []);

    return (
        <div className={"profile-page"}>
            <div className={"profile-card"}>
                <h1>{user?.username}'s profile</h1>
                <p>{user?.email}</p>

                <div className={"profile-stats"}>
                    <div className={"stat"}>
                       <h1>{user?.totalProblemsSolved} <h2>problems solved</h2></h1>
                    </div>

                    <div className={"stat"}>
                        <h1>{user?.totalRevisions} <h2>total revisions</h2></h1>
                    </div>
                </div>
            </div>

                <button className={"logout-button"} onClick={handleLogout}>
                    Logout
                </button>
        </div>
    );
}
export default Profile;