import { Link, NavLink, useLocation } from 'react-router-dom';
import { useState, useEffect } from 'react';

function Navbar() {
    const location = useLocation();
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [sidebarOpen, setSidebarOpen] = useState(false);

    useEffect(() => {
        setSidebarOpen(false);
    }, [location.pathname]);

    useEffect(() => {
        document.body.style.overflow = sidebarOpen ? "hidden" : "";

        return () => {
            document.body.style.overflow = "";
        };
    }, [sidebarOpen]);

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
            <nav
                className="navbar"
                onClick={() => sidebarOpen && setSidebarOpen(false)}
            >

                <div className="navbar-brand">

                    <button
                        className="menu-button"
                        onClick={(event) => {
                            event.stopPropagation();
                            setSidebarOpen(!sidebarOpen);
                        }}
                        aria-label="Toggle navigation menu"
                        aria-expanded={sidebarOpen}
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

                        <>
                            <NavLink to="/problems">
                                Problems
                            </NavLink>
                            <NavLink to="/my-problems">
                                My problems
                            </NavLink>
                            <NavLink to="/Profile">
                                <img
                                    className="profile-icon"
                                    src="/profileLogo.svg"
                                    alt="Profile"
                                />
                            </NavLink>
                        </>
                    ) : (
                        <>
                            <NavLink to="/login">Login</NavLink>
                            <NavLink to="/register">Sign up</NavLink>
                        </>
                    )}

                </div>

            </nav>

            <div
                className={`sidebar-overlay ${sidebarOpen ? "open" : ""}`}
                onClick={() => setSidebarOpen(false)}
                aria-hidden={!sidebarOpen}
            >
                <aside
                    className="sidebar"
                    aria-label="Navigation menu"
                    onClick={(event) => event.stopPropagation()}
                >
                    <Link to="/problems">Problems</Link>
                    <hr className="sidebar-divider" />
                    <Link to="/my-problems">My Problems</Link>
                </aside>
            </div>
        </>
    );
}

export default Navbar;
