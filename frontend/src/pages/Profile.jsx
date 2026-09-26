import {Link} from "react-router-dom";
import {useState, useEffect} from "react";
import { useNavigate } from "react-router-dom";

function Profile() {
    const [user, setUser] = useState(null);
    const [activity, setActivity] = useState([]);
    const navigate = useNavigate();

    function handleLogout() {
        localStorage.removeItem('token');
        window.dispatchEvent(new Event("authChange"));
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
    const startDay = startDate.getDay();
    startDate.setDate(startDate.getDate() - startDay);

    for(let date = new Date(startDate); date <= today; date.setDate(date.getDate() + 1)) {
        dates.push(new Date(date));
    }

//place every cell explicitly by row and column
    const heatmapData = [];
    let column = 0;

    for(let index = 0; index < dates.length; index++) {
        const date = dates[index];
        const row = date.getDay();

        let monthChanged = false;

        if(index > 0) {
            const previousDate = dates[index - 1];
            monthChanged =
                date.getMonth() !== previousDate.getMonth() ||
                date.getFullYear() !== previousDate.getFullYear();
            const newWeek = row === 0;

            if(monthChanged || newWeek) {
                column++;
            }
        }

        const dateKey = date.toISOString().split("T")[0];

        heatmapData.push({
            date: dateKey,
            revisionCount: activityMap[dateKey] || 0,
            column: column,
            row: row,
            monthStart: monthChanged
        });
    }

//heatmap intensity logic
    const getIntensity = (count) => {
        if(count === 0) return "level-0";
        if(count === 1) return "level-1";
        if(count <= 3) return "level-2";
        return "level-4";
    }

//month labels
    const monthLabels = [];

    heatmapData.forEach((day, index) => {
        if(index === 0) return;
        const date = new Date(day.date);

        if (
            index === 0 ||
            date.getMonth() !== new Date(heatmapData[index - 1].date).getMonth() ||
            date.getFullYear() !== new Date(heatmapData[index - 1].date).getFullYear()
        ) {
            monthLabels.push({
                month: date.toLocaleString("default", {
                    month: "short"
                }),
                column: day.column
            });
        }
    });

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

            <div className={"profile-card"}>
                <div className="revision-progress">
                    <h2>Revision Progress</h2>

                    <div className="streak-stats">
                        <div className="streak-stat">
                            <span className="streak-label">Current Streak: {user?.currentStreak} days</span>
                        </div>

                        <div className="streak-stat">
                            <span className="streak-label">Best Streak: {user?.maxStreak} days</span>
                        </div>
                    </div>

                    <div className="heatmap-container">

                        <div className="month-labels">
                            {monthLabels.map((month, index) => (
                                <span
                                    key={`${month.month}-${month.column}-${index}`}
                                    style={{ gridColumn: month.column + 1 }}
                                >
                                {month.month}
                                </span>
                            ))}
                        </div>

                        <div className="heatmap">
                            {heatmapData.map((day) => (
                                <div
                                    key={day.date}
                                    className={`heatmap-cell ${getIntensity(day.revisionCount)} ${
                                        day.monthStart ? "month-start" : ""
                                    }`}
                                    style={{
                                        gridColumn: day.column + 1,
                                        gridRow: day.row + 1
                                    }}
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
