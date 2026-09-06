import {useState} from "react";
import {useNavigate, useNavigation} from "react-router-dom";

function Register() {

    //property methods
    const [username, setUsername] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')

    const navigate = useNavigate()

    //input handle method
    async function handleSubmit(event) {
        event.preventDefault() //preventing default html page loading after form submission

        const user = {//user object
            username,
            email,
            password
        }

        const response = await
            fetch('http://localhost:8080/api/users', { //fetch post user endpoint
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(user)
            })

        const data = await response.json();

        if(response.ok) {
            navigate('/register-success', {
                state: {username: data.username}
            })
        }else {
            console.log(data)
        }
    }

    return (
        <main className="auth-page">
            <div className="auth-card">
                <h1>Create your account</h1>
                <p>Start tracking your DSA journey.</p>

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="username">Username</label>
                        <input
                            id="username"
                            type="text"
                            value={username}
                            onChange={event => //set username
                                setUsername(event.target.value)}
                            placeholder="Enter your username"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <input
                            id="email"
                            type="email"
                            value={email}
                            onChange={event => //set email
                                setEmail(event.target.value)}
                            placeholder="Enter your email"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            id="password"
                            type="password"
                            value={password}
                            onChange={event => //set password
                                setPassword(event.target.value)}
                            placeholder="Enter your password"
                        />
                    </div>

                    <button type="submit">Create Account</button>
                </form>
            </div>
        </main>
    )
}

export default Register