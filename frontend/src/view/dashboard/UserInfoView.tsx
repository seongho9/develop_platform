import { Title } from "../../components/commonComponent/Title";
import { Button } from "../../components/commonComponent/Button";
import { Input } from "../../components/commonComponent/Input";
import { InputForm } from "../../components/memberPage/InputForm";
import { SubmitContainer } from "../../components/memberPage/SubmitContainer";
import { ChangeEvent } from "react";
import { Box, UnderBox, UpperBox } from "../../components/dashboard/Body";
import { TitleContainer } from "../../components/dashboard/Main";
import { UserInfoForm } from "../../components/dashboard/UserInfo";

interface UserInfoViewProps {
  id: string;
  passwordBefore: string;
  passwordAfter: string;
  email: string;
  idHandler: (e: ChangeEvent<HTMLInputElement>)=>void;
  passwordBeforeHandler: (e: ChangeEvent<HTMLInputElement>)=>void;
  passwordAfterHandler: (e: ChangeEvent<HTMLInputElement>)=>void;
  emailHandler: (e: ChangeEvent<HTMLInputElement>)=>void;
  submitHandler: ()=>void;
  
}
export default function UserInfoView(props: UserInfoViewProps){

    return (
        <Box>
          <UpperBox>
            <TitleContainer>
              <Title>User Infomation</Title>
            </TitleContainer>
            <UserInfoForm>
                <InputForm>
                    <Input type="text" placeholder="id *" value={props.id} onChange={props.idHandler}/>
                    <Input type="password" placeholder="before password *" value={props.passwordBefore} onChange={props.passwordBeforeHandler}/>
                    <Input type="password" placeholder="after password *" value={props.passwordAfter} onChange={props.passwordAfterHandler}/>
                    <Input type="text" placeholder="email *" value={props.email} onChange={props.emailHandler}/>
                </InputForm>
                <SubmitContainer>
                    <Button onClick={props.submitHandler}>Submit</Button>
                </SubmitContainer>
            </UserInfoForm>
          </UpperBox>
          <UnderBox />
        </Box>
    );
}