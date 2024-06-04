export type JWT = {
    sub: string,
    id: string,
    exp: string
};

export type Login = {
  access: string,
  refresh: string,
};

export type MemberInfo = {
    id: string,
    userName: string,
    mail: string,
};

export const baseUrl = "https://api.seongho9.me";