import { Link } from 'react-router-dom'
import { useState, useEffect } from 'react'

function Navbar() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

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
        <nav className="navbar">
            <Link to="/" className="logo">
                <img src="/dsaclockLogoFinal.svg" alt="DSAClock" />
            </Link>

            <div className="nav-links">
                {isLoggedIn ? (
                    <Link to="/Profile">
                        <img className={"profile-icon"} src={"/profileLogo.svg"} alt="Profile" />
                    </Link>
                ) : (
                    <>
                        <Link to="/login">Login</Link>
                        <Link to="/register">Register</Link>
                    </>
                )}
            </div>
        </nav>
    )
}

export default Navbar