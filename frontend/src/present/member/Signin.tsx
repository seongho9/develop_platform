import { ChangeEvent, useState } from "react";
import SigninView from "../../view/SigninView";
import { Login } from "../../Request/Types";
import Post from "../../Request/Post";
import base64 from "base-64";
import { useNavigate } from "react-router-dom";

type loginInfo = {
    userId:string,
    passwd: string
};
type JWT = {
    sub: string,
    id: string,
    exp: string
  };
export default function Signin(){

    const navigator = useNavigate();

    const [info, setInfo] = useState<loginInfo>({
        userId: "",
        passwd: "",
    });

    const handleidInput = (e: ChangeEvent<HTMLInputElement>) =>{
        setInfo(prev=>({
            userId:e.target.value,
            passwd: prev.passwd
        }));
    }

    const handlepwInput = (e: ChangeEvent<HTMLInputElement>) =>{
        setInfo(prev =>({
            userId:prev.userId,
            passwd: e.target.value
        }));
    }

    const handleSubmit = async () => {
        
        await Post("/member/login", info)
            .then(res=> res.json())
            .then(data=>{
                const access = data.access;
                const payload = access.substring(access.indexOf(".")+1,access.lastIndexOf("."));
                const decode:JWT = JSON.parse(base64.decode(payload));
        
                sessionStorage.clear();
                sessionStorage.setItem("access", data.access);
                sessionStorage.setItem("exp", decode.exp);
                sessionStorage.setItem("refresh", data.refresh);
        
                navigator("/dashboard?page=main");
            })
            .catch(err => {
                console.error(err);
                alert("로그인 실패!");
                navigator("/");
            });
    }

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) =>{
        if(e.key == 'Enter'){
            handleSubmit();
        }
    }
    return(
        <SigninView 
            id={info.userId} 
            password={info.passwd} 
            idHandler={handleidInput} 
            passwordHandler={handlepwInput}
            handleKeyDown={handleKeyDown}
            submitHandler={handleSubmit}
        />
    );
}