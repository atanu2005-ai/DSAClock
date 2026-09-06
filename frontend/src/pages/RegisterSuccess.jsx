import {useLocation} from "react-router-dom";

function RegisterSuccess() {
    const location = useLocation()
    const username = location.state?.username
    return (
        <main>
            <h1>Registration successful, welcome {username}!</h1>
        </main>
    )
}

export default RegisterSuccess