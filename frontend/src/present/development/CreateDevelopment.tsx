
import React, { useEffect, useState } from 'react'
import CreateDevelopView, { image } from '../../view/dashboard/CreateDevelopmentView'
import Get from '../../Request/Get';
import { useNavigate } from 'react-router-dom';
import Post from '../../Request/Post';

export default function CreateDevelopment() {

    const navigate = useNavigate();

    
    const [selectedOption, setOption] = useState<string>("Current Value");
    const [isOpen, setIsOpen] = useState(false);

    const [imgs, setImg] = useState<image[]>([]);

    const handleOption = (e: React.MouseEvent<HTMLLIElement>) =>{
        const text = e.currentTarget.getAttribute("value");
        if(text === null){
          setOption("");
        }
        else{
          setOption(text);
        }
    }

    const handleSubmit = async () => {
        Post("/dev/create", {imageName: selectedOption})
        .then(res=> res.text())
        .then(data=>{
            alert(data);
            console.log(data);
            navigate("/dashboard?page=main");
        })
        .catch(err=>{
            alert(err);
            console.log(err);
            navigate("/dashboard?page=dev");
        });
    }

    useEffect(()=>{
        let cnt = 0;

        Get("/dev/images")
            .then(res=>res.json())
            .then((data:string[])=>{
                data.map(item => {
                    console.log(item);
                    setImg(prev=>([
                        ...prev,
                        {key: cnt++, value:item}
                    ]));
                })
                console.log(imgs);
            })
            .catch(err => {
                alert("failed to get image data");
                navigate("/dashboard?page=main");
            });

    }, []);

    return (
        <CreateDevelopView 
            images={imgs}
            isOpen={isOpen}
            select={selectedOption}
            handleOption={handleOption}
            handleSubmit={handleSubmit}
        />
    );
}
