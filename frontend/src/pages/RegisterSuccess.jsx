import {Link, useLocation} from "react-router-dom";

function RegisterSuccess() {
    const location = useLocation()
    const username = location.state?.username
    return (
        <main>
            <h1>Registration successful, welcome {username}!</h1>
            
            <Link to={"/problems"} className={"hero-buttons"}>
                Go to Problems
            </Link>
        </main>
    )
}

export default RegisterSuccess