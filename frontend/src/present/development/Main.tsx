import { useEffect, useState } from "react";
import MainView from "../../view/dashboard/MainView";
import Get from "../../Request/Get";
import Post from "../../Request/Post";
import Delete from "../../Request/Delete";

type Container = {
    id: string,
    userId: string,
    name: string, 
    running: string,
};
type DevleopmentEnv = {
    key: number,
    id: string,
    userId: string,
    name: string,
    status: string
};
export default function Main() {

    const [containerList, setContainer] = useState<DevleopmentEnv[]>([]);

    useEffect(()=>{

        Get("/dev/list")
        .then((data:Response)=>(data.json()))
        .then((json:Container[])=>{
            let count = 0;
            console.log(json);
            json.map((item: Container)=>
                setContainer(prev=>[
                    ...prev,
                    {
                        key: count++,
                        id: item.id,
                        userId: item.userId,
                        name: item.name,
                        status: item.running? "Running" : "NotRunning"
                    }
                ])
            );
        })
        .catch(err=>console.log(err));
    },[]);

    const handleStart = (id:string, url:string) => {
        Post(`/dev/start?id=${id}`, null)
        .then(res=>res.text())
        .then(data=>{
          setTimeout(()=>{
            window.open(url);
            window.location.reload();
          }, 1000);
        })
        .catch(err=>{
            console.log(err);
            alert("개발환경 실행실패");
            window.location.reload();
        })
    };

    const handleStop = (id:string) => {
        Post(`/dev/stop?id=${id}`, null)
        .then(res=>{
            window.location.reload();
        })
        .catch(err=>{
            console.error(err);
            alert("종료실패");
            window.location.reload()
        });
    };

    const handleDelete = (id:string, image: string) =>{
        const body = {  id:id, imageName: image };

        Delete('/dev/delete', body)
          .then(res=>{
            window.location.reload();
          })
          .catch(err=>{
            console.error(err);
            alert("삭제 실패");
            window.location.reload();
          });
    };

    return (
        <MainView list={containerList} handleStart={handleStart} handleStop={handleStop} handleDelete={handleDelete}/>
    );
}
