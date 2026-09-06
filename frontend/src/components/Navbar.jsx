import { Link } from 'react-router-dom'

function Navbar() {
    return (
        <nav className="navbar">
            <Link to="/" className="logo">
                DSAClock
            </Link>

            <div className="nav-links">
                <Link to="/login">Login</Link>
                <Link to="/register">Register</Link>
            </div>
        </nav>
    )
}

export default Navbar