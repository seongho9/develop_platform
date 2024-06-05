import { Title } from "../components/commonComponent/Title";
import { Button } from "../components/commonComponent/Button";
import { Input } from "../components/commonComponent/Input";
import { InputForm } from "../components/memberPage/InputForm";
import { SigninContainer } from "../components/memberPage/SigninContainer";
import { SigninForm } from "../components/memberPage/SigninForm";
import { SubmitContainer } from "../components/memberPage/SubmitContainer";
import { TitleFontContainer } from "../components/memberPage/TitleContainer";
import { ChangeEvent } from "react";

interface SignupViewProps {
    id: string;
    password: string;
    name: string;
    email: string;
    idHandler: (e: ChangeEvent<HTMLInputElement>)=>void;
    passwordHandler: (e: ChangeEvent<HTMLInputElement>)=>void;
    nameHandler: (e: ChangeEvent<HTMLInputElement>)=>void;
    emailHandler: (e: ChangeEvent<HTMLInputElement>)=>void;
    submitHandler: ()=>void;
};

export default function SignupView(props: SignupViewProps){

    
    return(
        <SigninContainer>
            <SigninForm>
                <TitleFontContainer>
                    <Title>Sign up</Title>
                </TitleFontContainer>
                <InputForm>
                    <Input type="text" placeholder="full name *" value={props.name} onChange={props.nameHandler}/>
                    <Input type="text" placeholder="id *" value={props.id} onChange={props.idHandler}/>
                    <Input type="password" placeholder="password *" value={props.password} onChange={props.passwordHandler}/>
                    <Input type="text" placeholder="email *" value={props.email} onChange={props.emailHandler}/>
                </InputForm>
                <SubmitContainer>
                    <Button onClick={props.submitHandler}>Submit</Button>
                </SubmitContainer>

            </SigninForm>
        </SigninContainer>
    );
}