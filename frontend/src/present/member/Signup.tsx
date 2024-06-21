import { ChangeEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import Post from "../../Request/Post";
import SignupView from "../../view/SignupView";

type SignupInfo = {
    userId: string,
    passwd: string,
    name: string,
    mail: string
};


export default function Signup() {

    const navigate = useNavigate();

    const [info, setInfo] = useState<SignupInfo>({
        userId: "",
        passwd: "",
        mail: "",
        name: ""
    });
    const idHandler = (e:ChangeEvent<HTMLInputElement>) => {
        setInfo(prev=>({
            userId: e.target.value,
            passwd: prev.passwd,
            mail: prev.mail,
            name: prev.name
        }));
    }
    const pwHandler = (e:ChangeEvent<HTMLInputElement>) => {
        setInfo(prev=>({
            userId: prev.userId,
            passwd: e.target.value,
            mail: prev.mail,
            name: prev.name
        }));
    }
    const nameHandler = (e:ChangeEvent<HTMLInputElement>) => {
        setInfo(prev=>({
            userId: prev.userId,
            passwd: prev.passwd,
            mail: prev.mail,
            name: e.target.value,
        }));
    }
    const mailHandler = (e:ChangeEvent<HTMLInputElement>) => {
        setInfo(prev=>({
            userId: prev.userId,
            passwd: prev.passwd,
            mail: e.target.value,
            name: prev.name
        }));
    }
    const onSubmit = async () => {
        const reqHeader = new Headers();
        reqHeader.set("Content-Type","application/json");

        await Post("/member/signup", info)
            .then(res=> res.text())
            .then(text=>{
                console.log(text);
                navigate("/");
            })
            .catch(err=>{
                console.error(err);
                alert("회원가입 실패");
            });
    }

    return(
        <SignupView 
            id={info.userId} password={info.passwd} name={info.name} email={info.mail}
            idHandler={idHandler} 
            passwordHandler={pwHandler}
            nameHandler={nameHandler}
            emailHandler={mailHandler}
            submitHandler={onSubmit}
        />
    )
}  