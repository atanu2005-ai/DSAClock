import {Link} from "react-router-dom";

function Home() {
    return (
        <main className={"hero"}>
            <h1>DSAClock</h1>
            <p>
                Track your DSA journey
                <br/>
                Practice. Track. Improve.
            </p>

            <div className={"Hero-buttons-container"}>
                <div className={"start-buttons"}>
                    <Link to={"/register"} className={"hero-buttons"}>
                        Get started
                    </Link>
                </div>

                <div className={"problem-buttons"}>
                    <Link to={"/login"} className={"hero-buttons"}>
                        Login
                    </Link>
                    <Link to={"/problems"} className={"hero-buttons"}>
                        Problems
                    </Link>
                </div>
            </div>

        </main>
    )
}

export default Home