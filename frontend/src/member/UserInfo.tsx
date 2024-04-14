import * as React from 'react';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { MemberInfo } from '../Request/Types';
import Get from '../Request/Get';

import Title from '../common/Title';
import { Box, Button, Grid, TextField } from '@mui/material';
import Post from '../Request/Post';

type ChangedMember = {
  userId: string,
  passwdBefore: string,
  passwdAfter: string,
  mail: string,
  name: string,
}
export default function UserInfo() {

  const navigate = useNavigate();

  const [info, setInfo] = useState<ChangedMember>({
    userId:"",
    passwdBefore:"",
    passwdAfter:"",
    name:"",
    mail:""
  });
  
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log(info);
    Post("/member/modify", {
      userId: info.userId,
      passwdBefore: info.passwdBefore,
      passwdAfter: info.passwdAfter,
      mail: info.mail
    })
    .then(res=>res.text())
    .then(data=>{
      alert(data)
      navigate("/dashboard?page=main");
    })
    .catch(err=>{
      alert(err.message);
      navigate("/dashboard?page=user");
    })
  };
  useEffect(()=>{
    Get("/member/info")
    .then(res=>res.json())
    .then((data:MemberInfo)=>{
      setInfo(prev => ({
        userId:data.id,
        passwdBefore:"",
        passwdAfter:"",
        name: data.userName,
        mail: data.mail
      }));
      console.log(data);
    })
    .catch(err=>{
      console.log(err);
      alert(err)
    });
  },[]);

  return (
    <React.Fragment>
      <Title>User Infomation</Title>
      <Box component="form" 
        noValidate onSubmit={handleSubmit} 
        autoComplete="off"
        sx={{ mt: 3, display:'flex', flexDirection:'column',  alignItems:'center' }}>
            <Grid container spacing={2} sx={{width:'60%'}}>
              <Grid item xs={12}>
                <TextField
                  required
                  InputLabelProps={{shrink: true,}}
                  InputProps={{readOnly: true,}}
                  id="name"
                  label="full name"
                  value={info.name}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  InputLabelProps={{shrink: true,}}
                  InputProps={{readOnly: true,}}
                  fullWidth
                  id="userId"
                  label="id"
                  value={info.userId}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  InputLabelProps={{shrink: true,}}
                  id="passwordBefore"
                  name="passwordBefore"
                  label="Password Before"
                  type="password"
                  value={info.passwdBefore}
                  onChange={(event:React.ChangeEvent<HTMLInputElement>)=>{
                    setInfo(prev=>({
                      userId:prev.userId,
                      passwdBefore: event.target.value,
                      passwdAfter: prev.passwdAfter,
                      name: prev.name,
                      mail: prev.mail
                    }))
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  InputLabelProps={{shrink: true,}}
                  id="passwordAfter"
                  name="passwordAfter"
                  label="Password After"
                  type="password"
                  value={info.passwdAfter}
                  onChange={(event:React.ChangeEvent<HTMLInputElement>)=>{
                    setInfo(prev=>({
                      userId:prev.userId,
                      passwdBefore: prev.passwdBefore,
                      passwdAfter: event.target.value,
                      name: prev.name,
                      mail: prev.mail
                    }))
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  InputLabelProps={{shrink: true,}}
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  value={info?.mail}
                  onChange={(event: React.ChangeEvent<HTMLInputElement>)=>{
                    setInfo(prev => ({
                      userId:prev.userId,
                      passwdBefore: prev.passwdBefore,
                      passwdAfter: prev.passwdAfter,
                      name: prev.name,
                      mail: event.target.value
                    }));
                  }}
                />
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