import { Link, useLocation } from 'react-router-dom';
import { useState, useEffect } from 'react';

function Navbar() {
    const location = useLocation();
    useEffect(() => {
        setSidebarOpen(false);
    }, [location.pathname]);

    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [sidebarOpen, setSidebarOpen] = useState(false);

    useEffect(() => {
        const checkAuth = () => {
            const token = localStorage.getItem("token");

            if (!token) {
                setIsLoggedIn(false);
                return;
            }

            fetch("http://localhost:8080/api/users/me", {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Invalid token");
                    }

                    setIsLoggedIn(true);
                })
                .catch(() => {
                    localStorage.removeItem("token");
                    setIsLoggedIn(false);
                });
        };

        checkAuth();

        window.addEventListener("authChange", checkAuth);

        return () => {
            window.removeEventListener("authChange", checkAuth);
        };
    }, []);

    return (
        <>
            <nav className="navbar">

                <div className="navbar-brand">

                    <button
                        className="menu-button"
                        onClick={() => setSidebarOpen(!sidebarOpen)}
                    >
                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            width="50"
                            height="50"
                            viewBox="0 0 50 50"
                        >
                            <path
                                d="M0,9v2h50v-2zM0,24v2h50v-2zM0,39v2h50v-2z"
                                fill="currentColor"
                            />
                        </svg>
                    </button>

                    <Link to="/" className="logo">
                        <img
                            src="/dsaclockLogoFinal.svg"
                            alt="DSAClock"
                        />
                    </Link>

                </div>

                <div className="nav-links">

                    {isLoggedIn ? (
                        <Link to="/Profile">
                            <img
                                className="profile-icon"
                                src="/profileLogo.svg"
                                alt="Profile"
                            />
                        </Link>
                    ) : (
                        <>
                            <Link to="/login">Login</Link>
                            <Link to="/register">Register</Link>
                        </>
                    )}

                </div>

            </nav>

            <aside className={`sidebar ${sidebarOpen ? "open" : ""}`}>
                <Link to="/problems">
                    Problems
                </Link>

                <Link to="/my-problems">
                    My Problems
                </Link>

            </aside>
        </>
    );
}

export default Navbar;