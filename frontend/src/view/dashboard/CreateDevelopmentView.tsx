import { ChangeEvent, useState } from "react";
import { Title } from "../../components/commonComponent/Title";
import { Box, UnderBox, UpperBox } from "../../components/dashboard/Body";
import { TitleContainer } from "../../components/dashboard/Main";
import { Label, Option, SelectBox, SelectOptions } from "../../components/commonComponent/Select";
import { Handshake } from "@mui/icons-material";
import { Button } from "../../components/commonComponent/Button";
import { SubmitContainer } from "../../components/dashboard/Create";

interface CreateDevelopViewProps{
  images:image[];
  isOpen: boolean;
  select: string;
  handleSubmit: ()=>void;
  handleOption: (e:React.MouseEvent<HTMLLIElement>)=>void;
};
export type image = {
  key: number;
  value: string;
}
export default function CreateDevelopView(props: CreateDevelopViewProps) {

  const [isOpen, setIsOpen] = useState<boolean> (false);


  const handleOpen = () =>{
    setIsOpen(prev=>!prev);
}
  return (
    <Box>
      <UpperBox>
        <TitleContainer>
          <Title> Create  Environment</Title>
        </TitleContainer>
        <SelectBox onClick={handleOpen}>
          <Label>{props.select}</Label>
          <SelectOptions show={isOpen}>
            { 
              props.images.map((data)=>(
                <Option key={data.key} value={data.value} onClick={props.handleOption}>
                  {data.value}
                </Option>
              ))
            }
          </SelectOptions>
        </SelectBox>
        <SubmitContainer>
          <Button onClick={props.handleSubmit}>Create</Button>
        </SubmitContainer>
      </UpperBox>
      <UnderBox>
      </UnderBox>
    </Box>
  )
}
