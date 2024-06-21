import { useState } from "react";
import { Container } from "../components/dashboard/Container";
import { Header, Menu, TitleContainer } from "../components/dashboard/Header";
import { GiHamburgerMenu } from "react-icons/gi";
import { Title } from "../components/commonComponent/Title";
import { MdKeyboardArrowLeft } from "react-icons/md";
import { MdCode } from "react-icons/md";
import { MdPeople } from "react-icons/md";
import { MdDashboard } from "react-icons/md";
import { MdOutlineLogout } from "react-icons/md";
import { Body, Icon, Name, Side, Content } from "../components/dashboard/Body";
import Main from "../present/development/Main";
import CreateDevelopment from "../present/development/CreateDevelopment";
import { useNavigate } from "react-router-dom";
import SignupView from "./SignupView";
import UserInfo from "../present/development/UserInfo";
import Post from "../Request/Post";

interface DashBoardViewProps {
    page: string | null;
};

export default function DashBoradView(props:DashBoardViewProps){

    const [isOpen, setOpen] = useState<boolean>(false);

    const navigator =  useNavigate();

    const onClickDrawer = () =>{
        setOpen(!isOpen);
    }
    return(
        <Container>
            <Header isOpen={isOpen}>
                <Menu isOpen={isOpen} onClick={onClickDrawer}>
                    { isOpen && <MdKeyboardArrowLeft />}
                    { !isOpen && <GiHamburgerMenu />}
                </Menu>
                <TitleContainer>
                    <Title>DashBoard</Title>
                </TitleContainer>
            </Header>
            <Body>
                <Side isOpen={isOpen}>
                    <Icon isOpen={isOpen} onClick={()=>{navigator("/dashboard?page=main")}} >
                        <MdDashboard />
                        { isOpen ? 
                            <Name> Dashboard </Name> : null}
                    </Icon>
                    <Icon isOpen={isOpen} onClick={()=>{navigator("/dashboard?page=create")}}>
                        <MdCode />
                        { isOpen ? 
                            <Name> Development </Name> : null}
                    </Icon>
                    <Icon isOpen={isOpen} onClick={()=>{navigator("/dashboard?page=userInfo")}}>
                        <MdPeople />
                        { isOpen ? 
                            <Name> User Info </Name> : null}
                    </Icon>
                    <Icon isOpen={isOpen} onClick={()=>{
                        Post("/member/logout",null)
                        .then(res=>{
                        sessionStorage.clear()
                        navigator("/");
                        })
                    }}>
                        <MdOutlineLogout />
                        { isOpen ? 
                            <Name> Logout </Name> : null}
                    </Icon>
                </Side>
                <Content isOpen={isOpen}>
                    {
                        props.page === "main"
                            && 
                        <Main />
                    }
                    
                    {
                        props.page === "create"
                            &&
                        <CreateDevelopment />
                    }
                    {
                        props.page === "userInfo"
                            &&
                        <UserInfo />
                    }
                </Content>
            </Body>
        </Container>
    );
}