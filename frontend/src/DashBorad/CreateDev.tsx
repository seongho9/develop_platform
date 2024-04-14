import * as React from 'react';
import { useEffect } from 'react';
import Button from '@mui/material/Button';
import { useNavigate } from 'react-router-dom';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import { createTheme } from '@mui/material/styles';
import Title from '../common/Title';
import { MenuItem, Select, SelectChangeEvent } from '@mui/material';
import Get from '../Request/Get';
import Post from '../Request/Post';

// TODO remove, this demo shouldn't need to reset the theme.
const defaultTheme = createTheme();

type Image = {
    id: number,
    name: string
};
export default function CreateDev() {

  const navigate = useNavigate();

  const [dev, setDev] = React.useState<string>("");

  const [imgs, setImg] = React.useState<Image[]>([]);

  useEffect(()=>{
    let cnt = 0;
    Get("/dev/images")
    .then(res=>res.json())
    .then((data:string[])=>{
        data.map(item=>{
            setImg(prev=>([
                ...prev,
                {id: cnt++, name:item}
            ]));
        })
    })
    .catch(err => {
        alert("failed to get image data");
        navigate("/dashboard?page=main");
    });

  },[]);
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    Post("/dev/create", {imageName: dev})
    .then(res=> res.text())
    .then(data=>{
        alert(data);
        navigate("/dashboard?page=main");
    })
    .catch(err=>{
        alert(err);
        navigate("/dashboard?page=dev");
    })
  };

  return (
    <React.Fragment>
      <Title>Create Developments</Title>
      <Box component="form" 
        noValidate onSubmit={handleSubmit} 
        autoComplete="off"
        sx={{ mt: 3, display:'flex', flexDirection:'column',  alignItems:'center' }}>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <Select
                    labelId="image"
                    id="image"
                    value={dev}
                    label="environments"
                    sx={{width:'60%'}}
                    onChange={(event:SelectChangeEvent)=>{
                        console.log(event.target.value);
                        setDev(event.target.value);
                    }}>
                    {
                        imgs.map(item=>(
                            <MenuItem key={item.id} value={item.name}>{item.name}</MenuItem>
                        ))
                    }
                </Select>
              </Grid>
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2, width:'30%' }}
            >
              submit
            </Button>
          </Box>
    </React.Fragment>
  );
}