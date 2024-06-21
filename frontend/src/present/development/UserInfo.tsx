import React, { ChangeEvent, useEffect, useState } from 'react'
import UserInfoView from '../../view/dashboard/UserInfoView'
import Post from '../../Request/Post';
import Get from '../../Request/Get';
import { MemberInfo } from '../../Request/Types';

type UserInfo = {
    userId: string;
    passwdBefore: string;
    passwdAfter: string;
    mail: string;
}
export default function UserInfo() {
    const [info, setInfo] = useState<UserInfo>({
        userId: "",
        passwdBefore: "",
        passwdAfter: "",
        mail: "",
    });
    
    useEffect(()=>{
        Get("/member/info")
        .then(res=>res.json())
        .then((data:MemberInfo)=>{
        setInfo(prev => ({
            userId:data.id,
            passwdBefore:"",
            passwdAfter:"",
            name: data.userName,
            mail: data.mail
        }));
        console.log(data);
        })
        .catch(err=>{
        console.log(err);
        alert(err)
        });
    },[]);

    const idHandler = (e:ChangeEvent<HTMLInputElement>) => {
        setInfo(prev=>({
            userId: e.target.value,
            passwdBefore: prev.passwdBefore,
            passwdAfter: prev.passwdAfter,
            mail: prev.mail,
        }));
    }
    const pwBeforeHandler = (e:ChangeEvent<HTMLInputElement>) => {
        setInfo(prev=>({
            userId: prev.userId,
            passwdBefore: e.target.value,
            passwdAfter: prev.passwdAfter,
            mail: prev.mail,
        }));
    }
    const pwAfterHandler = (e:ChangeEvent<HTMLInputElement>) => {
        setInfo(prev=>({
            userId: prev.userId,
            passwdBefore: prev.passwdBefore,
            passwdAfter: e.target.value,
            mail: prev.mail,
        }));
    }
    const mailHandler = (e:ChangeEvent<HTMLInputElement>) => {
        setInfo(prev=>({
            userId: prev.userId,
            passwdBefore: prev.passwdBefore,
            passwdAfter: prev.passwdAfter,
            mail: e.target.value,
        }));
    }
    const onSubmit = async () => {
        const reqHeader = new Headers();
        reqHeader.set("Content-Type","application/json");
        await Post("/member/modify", info)
            .then(res=> res.text())
            .then(text=>{
                console.log(text);
                window.location.reload();
            })
            .catch(err=>{
                console.error(err);
                alert("회원가입 실패");
            });
    }
  return (
    <UserInfoView 
        id={info.userId} passwordBefore={info.passwdBefore} passwordAfter={info.passwdAfter} email={info.mail}
        idHandler={idHandler}
        passwordBeforeHandler={pwBeforeHandler}
        passwordAfterHandler={pwAfterHandler}
        emailHandler={mailHandler}
        submitHandler={onSubmit}
    />
  )
}
