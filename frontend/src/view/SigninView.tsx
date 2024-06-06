import { Title } from "../components/commonComponent/Title";
import { Button } from "../components/commonComponent/Button";
import { Input } from "../components/commonComponent/Input";
import { Link } from "../components/commonComponent/Link";
import { InputForm } from "../components/memberPage/InputForm";
import { LinkContainer } from "../components/memberPage/LinkContainer";
import { Container } from "../components/memberPage/Container";
import { SigninForm } from "../components/memberPage/SigninForm";
import { SubmitContainer } from "../components/memberPage/SubmitContainer";
import { TitleFontContainer } from "../components/memberPage/TitleContainer";
import { ChangeEvent } from "react";

interface SigninViewProps {
    id: string;
    idHandler: (e: ChangeEvent<HTMLInputElement>)=>void;
    password: string,
    passwordHandler: (e: ChangeEvent<HTMLInputElement>)=>void;
    submitHandler: ()=>void;
};

export default function SigninView(props:SigninViewProps){

    return(
        <Container>
            <SigninForm>
                <TitleFontContainer>
                    <Title>Development Platform</Title>
                </TitleFontContainer>
                <InputForm>
                    <Input type="text" placeholder="id *" value={props.id} onChange={props.idHandler}  />
                    <Input type="password" placeholder="password *" value={props.password} onChange={props.passwordHandler} />
                </InputForm>
                <SubmitContainer>
                    <Button onClick={props.submitHandler}>LOGIN</Button>
                </SubmitContainer>

                <LinkContainer>
                    <Link href="/register">Don't have an account?</Link>
                </LinkContainer>
            </SigninForm>
        </Container>
    );
}