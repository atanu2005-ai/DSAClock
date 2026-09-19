import {Link} from "react-router-dom";
import {useState, useEffect} from "react";
import { useNavigate } from "react-router-dom";

function Profile() {
    const [user, setUser] = useState(null);
    const [activity, setActivity] = useState([]);
    const navigate = useNavigate();

    function handleLogout() {
        localStorage.removeItem('token');
        window.dispatchEvent(new Event("authChange")); // Dispatch a storage event to notify other tabs
        navigate("/login")
    }

    useEffect(() => {

        const token = localStorage.getItem('token');
        if (token) {
            fetch('http://localhost:8080/api/users/me', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
            .then(response => response.json())
            .then(data => {setUser(data)})
            .catch(error => console.error('Error fetching user profile:', error));
        }else {
            navigate("/login")
        }

    }, []);

    //state for user activity
    useEffect(() => {
        fetch("http://localhost:8080/api/users/me/activity", {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`
            }
        })
            .then(response => response.json())
            .then(data => setActivity(data))
            .catch(error => console.error("Error fetching activity:", error));
    }, []);

    //lookup object
    const activityMap = {};

    activity.forEach(item => {
        activityMap[item.date] = item.revisionCount;
    });

    const today = new Date();
    const startDate = new Date(today);
    startDate.setFullYear(today.getFullYear() - 1);
    console.log("start date: ", startDate);
    console.log("today: ", today);

    //now all 365 days
    const dates = [];
    for(let date = new Date(startDate); date <= today; date.setDate(date.getDate() + 1)) {
        dates.push(new Date(date));
    }

    const heatmapData = dates.map(date => {
        const dateKey = date.toISOString().split("T")[0];

        return {
            date: dateKey,
            revisionCount: activityMap[dateKey] || 0
        };
    })

    //heatmap intensity logic
    const getIntensity = (count) => {
        if(count === 0) return "level-0";
        if(count === 1) return "level-1";
        if(count <= 3) return "level-2"
        return "level-4";
    }


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

                <div className="revision-progress">
                    <h2>Revision Progress</h2>

                    <div className={"heatmap-container"}>
                        <div className="heatmap">
                            {heatmapData.map(day => (
                                <div
                                    key={day.date}
                                    className= {`heatmap-cell ${getIntensity(day.revisionCount)}`}
                                    title={`${day.date}: ${day.revisionCount} revisions`}
                                >
                                </div>
                            ))}
                        </div>
                    </div>

                    <div className={"revision-count"}>
                        {user?.revised} / {user?.totalProblemsSolved}
                        <p>problems revised at least once</p>
                    </div>

                    <div className="progress-ring">
                        <svg width="140" height="140">
                            <circle
                                className="progress-ring-bg"
                                cx="70"
                                cy="70"
                                r="60"
                            />

                            <circle
                                className="progress-ring-fill"
                                cx="70"
                                cy="70"
                                r="60"
                                style={{
                                    strokeDashoffset:
                                        377 - (377 * (user?.revisedPercentage ?? 0)) / 100
                                }}
                            />
                        </svg>

                        <div className="progress-text">
                            {(user?.revisedPercentage ?? 0).toFixed(1)}%
                        </div>
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