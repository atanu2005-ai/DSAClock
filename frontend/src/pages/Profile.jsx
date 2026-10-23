import {Link} from "react-router-dom";
import {useState, useEffect} from "react";
import { useNavigate } from "react-router-dom";

function Profile() {
    const [user, setUser] = useState(null);
    const [activity, setActivity] = useState([]);
    const navigate = useNavigate();
    const [showEditForm, setShowEditForm] = useState(false);
    const [editForm, setEditForm] = useState({
        username: "",
        about: "",
        leetcodeProfileUrl: "",
        githubProfileUrl: "",
        linkedinProfileUrl: "",
        twitterProfileUrl: ""
    });

    useEffect(() => {
        if(showEditForm && user) {
            setEditForm({
                username: user?.username || "",
                about: user?.about || "",
                leetcodeProfileUrl: user?.leetcodeProfileUrl || "",
                githubProfileUrl: user?.githubProfileUrl || "",
                linkedinProfileUrl: user?.linkedinProfileUrl || "",
                twitterProfileUrl: user?.twitterProfileUrl || ""
            });
        }
    }, [showEditForm, user]);

    async function handleEditSubmit(e) {
        e.preventDefault();

        const token = localStorage.getItem("token");

        const response = await fetch(
            "http://localhost:8080/api/users/me",
            {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(editForm)
            }
        );

        if (response.ok) {
            const updatedUser = await response.json();

            setUser(updatedUser);
            setShowEditForm(false);
        }
    }

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

    //for extracting username from URLs
    const getUsernameFromUrl = (url) => {
        if (!url) return "";

        try {
            const pathname = new URL(url).pathname;
            return pathname.split("/").filter(Boolean).pop();
        } catch {
            return url;
        }
    };

    useEffect(() => {
        document.body.style.overflow = showEditForm ? "hidden" : "";

        return () => {
            document.body.style.overflow = "";
        };
    }, [showEditForm]);

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
        if(count > 1 && count <= 3) return "level-2";
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

    const totalSolved = user?.totalProblemsSolved || 0;
    const totalRevised = user?.totalRevisions;
    const reviseRatio = totalSolved > 0 ? (totalRevised / totalSolved) : 0;
    return (
        <div className={"profile-page"}>
            <div className={"profile-left"}>
                <div className={"profile-card"}>

                    <div className={"profile-header"}>
                        <div className={"profile-info"}>
                            <h1>@{user?.username}</h1>
                        </div>

                        <button className ="edit-profile-button" onClick={() => setShowEditForm(true)}>
                            edit profile
                        </button>
                    </div>

                    <div className={"profile-info"}>
                        <h3>About</h3>
                        <hr className={"card-divider"}/>
                    </div>
                    <div className={"about-section"}>
                        <p>{user?.about || "Write something about you"}</p>
                    </div>

                    <div className={"profile-info"}>
                        <h3>Socials</h3>
                        <hr className={"card-divider"}/>
                    </div>
                    <div className={"social-links"}>
                        {user?.leetcodeProfileUrl && (
                            <a href={user.leetcodeProfileUrl}
                               target={"_blank"}
                               rel={"noreferrer"}
                                aria-label={"LeetCode"}
                            >
                                <img src={"LeetcodeIcon.svg"} alt={"Leetcode"}/>

                                <span>
                                    {getUsernameFromUrl(user.leetcodeProfileUrl)}
                                </span>
                            </a>
                        )}

                        {user?.githubProfileUrl && (
                            <a href={user.githubProfileUrl}
                               target={"_blank"}
                               rel={"noreferrer"}
                                aria-label={"GitHub"}
                            >
                                <img src={"GithubIcon.svg"} alt={"Github"}/>

                                <span>
                                    {getUsernameFromUrl(user.githubProfileUrl)}
                                </span>
                            </a>
                        )}

                        {user?.linkedinProfileUrl && (
                            <a href={user.linkedinProfileUrl}
                               target={"_blank"}
                               rel={"noreferrer"}
                                aria-label={"LinkedIn"}
                            >
                                <img src={"LinkedInIcon.svg"} alt={"LinkedIn"}/>

                                <span>
                                    {getUsernameFromUrl(user.linkedinProfileUrl)}
                                </span>
                            </a>
                        )}

                        {user?.twitterProfileUrl && (
                            <a href={user.twitterProfileUrl}
                               target={"_blank"}
                               rel={"noreferrer"}
                                aria-label={"X / Twitter"}
                            >
                                <img src={"TwitterIcon.svg"} alt={"Twitter/X"}/>

                                <span>
                                    {getUsernameFromUrl(user.twitterProfileUrl)}
                                </span>
                            </a>
                        )}
                    </div>
                </div>

                <button className={"logout-button"} onClick={handleLogout}>
                    Logout
                </button>
            </div>

            <div className={"profile-right"}>
                <div className={"profile-card"}>
                    <h1>{user?.username}'s stats</h1>
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

                        <hr className={"card-divider"}/>

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

                        <div className={"progress-info"}>
                            <div className={"progress-item"}>
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
                                <div className={"revision-count"}>
                                    {user?.revised} <span> out of </span>{user?.totalProblemsSolved}
                                    <p>problems revised at least once</p>
                                </div>
                            </div>

                            <div className={"progress-item"}>
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
                                                    377 - (377 * (user?.revisedTwicePercentage ?? 0)) / 100
                                            }}
                                        />
                                    </svg>

                                    <div className="progress-text">
                                        {(user?.revisedTwicePercentage ?? 0).toFixed(1)}%
                                    </div>

                                </div>
                                <div className={"revision-count"}>
                                    {user?.revisedAtLeastTwice}<span> out of </span>{user?.totalProblemsSolved}
                                    <p>problems revised at least twice</p>
                                </div>
                            </div>

                            <div className={"progress-item"}>
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
                                                    377 - (377 * (100 - (user?.revisedPercentage ?? 0))) / 100
                                            }}
                                        />
                                    </svg>

                                    <div className="progress-text">
                                        {(100 - user?.revisedPercentage ?? 0).toFixed(1)}%
                                    </div>

                                </div>
                                <div className={"revision-count"}>
                                    {user?.totalProblemsSolved - user?.revised} <span> out of </span>
                                    {user?.totalProblemsSolved}
                                    <p>problems are yet to be revised</p>
                                </div>
                            </div>

                            <div className="progress-item">
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
                                                    377 - (377 * Math.min(reviseRatio / 2, 1))
                                            }}
                                        />
                                    </svg>

                                    <div className="progress-text">
                                        {reviseRatio.toFixed(1)}
                                    </div>
                                </div>

                                <div className="revision-count">
                                    {totalRevised} <span> revisions for </span> {totalSolved}
                                    <p>problems with an average of {reviseRatio}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {showEditForm && (
                <div className="modal-overlay">
                    <div className="edit-profile-modal">
                        <h2>Edit Profile</h2>

                        <form onSubmit={handleEditSubmit}>

                            <label>Username</label>
                            <input
                                type="text"
                                value={editForm.username}
                                onChange={(e) =>
                                    setEditForm({
                                        ...editForm,
                                        username: e.target.value
                                    })
                                }
                            />

                            <label>About</label>
                            <textarea
                                value={editForm.about}
                                onChange={(e) =>
                                    setEditForm({
                                        ...editForm,
                                        about: e.target.value
                                    })
                                }
                            />

                            <label>LeetCode</label>
                            <input
                                type="url"
                                value={editForm.leetcodeProfileUrl}
                                onChange={(e) =>
                                    setEditForm({
                                        ...editForm,
                                        leetcodeProfileUrl: e.target.value
                                    })
                                }
                            />

                            <label>GitHub</label>
                            <input
                                type="url"
                                value={editForm.githubProfileUrl}
                                onChange={(e) =>
                                    setEditForm({
                                        ...editForm,
                                        githubProfileUrl: e.target.value
                                    })
                                }
                            />

                            <label>LinkedIn</label>
                            <input
                                type="url"
                                value={editForm.linkedinProfileUrl}
                                onChange={(e) =>
                                    setEditForm({
                                        ...editForm,
                                        linkedinProfileUrl: e.target.value
                                    })
                                }
                            />

                            <label>X / Twitter</label>
                            <input
                                type="url"
                                value={editForm.twitterProfileUrl}
                                onChange={(e) =>
                                    setEditForm({
                                        ...editForm,
                                        twitterProfileUrl: e.target.value
                                    })
                                }
                            />

                            <div className="edit-actions">
                                <button
                                    type="button"
                                    onClick={() => setShowEditForm(false)}
                                >
                                    Cancel
                                </button>

                                <button type="submit">
                                    Save Changes
                                </button>
                            </div>

                        </form>

                    </div>
                </div>
            )}

        </div>
    );
}

export default Profile;
