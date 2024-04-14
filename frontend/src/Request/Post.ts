import { Login, JWT, baseUrl } from "./Types";
import base64 from "base-64";

export default async function Post(url: string, body:any):Promise<Response>{

    const exp: string|null = sessionStorage.getItem("exp");

    if(exp!==null){
        const diff:number = parseInt(exp) * 1000 - Date.now();
        if(diff < 500){
            const refreshHeader: Headers = new Headers();

            const token = sessionStorage.getItem("access");
            const refresh = sessionStorage.getItem("refresh");

            if(token !== null && refresh !== null){
                refreshHeader.set("Authorization", "Bearer "+token);
                refreshHeader.set("refresh", "Bearer "+ refresh);
            }

            fetch(baseUrl + url, {headers:refreshHeader})
            .then(res => res.text())
            .then(token =>{
                sessionStorage.removeItem("access");
                sessionStorage.setItem("access", token);

                sessionStorage.removeItem("exp");
                const payload = token.substring(token.indexOf(".")+1,token.lastIndexOf("."));
                const decode:JWT = JSON.parse(base64.decode(payload));
                sessionStorage.removeItem("exp");
                sessionStorage.setItem("exp", decode.exp);
            })
        }
        const headers:Headers = new Headers();
        const token = sessionStorage.getItem("access");
        if(token !== null){
            if(body === null){
                headers.set("Authorization", "Bearer "+token);
                headers.set("Content-Type","application/json");
                const response = await fetch(baseUrl + url, {
                method:"POST",
                mode:"cors",
                credentials:"same-origin",
                headers:headers,
            });
            return response;
            }
            else{
                headers.set("Authorization", "Bearer "+token);
                headers.set("Content-Type","application/json");
                const response = await fetch(baseUrl + url, {
                    method:"POST",
                    mode:"cors",
                    credentials:"same-origin",
                    headers:headers,
                    body:body===null? null : JSON.stringify(body)
                });
                return response;
            }
        }
        else{
            return Promise.reject("로그인 먼저 하세요!");
        }
    }
    else{
        const header:Headers = new Headers();
        header.set("Content-Type","application/json");
        const response:Response = await fetch(baseUrl + url,{
            method: "POST",
            mode: "cors",
            credentials: "same-origin",
            headers:header,
            body: JSON.stringify(body)
        });

        return response;
    }
}