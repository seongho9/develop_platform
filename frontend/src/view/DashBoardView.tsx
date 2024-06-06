import { useEffect, useState } from "react";
import { Container } from "../components/dashboard/Container";
import { Header, Menu, Logout, TitleContainer } from "../components/dashboard/Header";
import { GiHamburgerMenu } from "react-icons/gi";
import { Title } from "../components/commonComponent/Title";
import { MdKeyboardArrowLeft } from "react-icons/md";
import { MdCode } from "react-icons/md";
import { MdPeople } from "react-icons/md";
import { MdDashboard } from "react-icons/md";
import { MdOutlineLogout } from "react-icons/md";
import { useNavigate, useSearchParams } from "react-router-dom";
import { Body, Icon, Name, Side, Content } from "../components/dashboard/Body";
import MainView from "./dashboard/MainView";


export default function DashBoradView(){

    const [isOpen, setOpen] = useState<boolean>(false);
    
    const [params] = useSearchParams();
    const page = params.get("page");
    const navigator = useNavigate();

    useEffect(()=>{
        console.log(page);
    },[]);
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
                    <Icon isOpen={isOpen} >
                        <MdDashboard />
                        { isOpen ? 
                            <Name> Dashboard </Name> : null}
                    </Icon>
                    <Icon isOpen={isOpen}>
                        <MdCode />
                        { isOpen ? 
                            <Name> Development </Name> : null}
                    </Icon>
                    <Icon isOpen={isOpen}>
                        <MdPeople />
                        { isOpen ? 
                            <Name> User Info </Name> : null}
                    </Icon>
                    <Icon isOpen={isOpen}>
                        <MdOutlineLogout />
                        { isOpen ? 
                            <Name> Logout </Name> : null}
                    </Icon>
                </Side>
                <Content isOpen={isOpen}>
                    <MainView list={[{    
                        key: 1,
                        id: "asdf",
                        userId: "string",
                        name: "string",
                        status: "Running"}]}/>
                </Content>
            </Body>
        </Container>
    );
}