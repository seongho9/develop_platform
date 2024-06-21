import styled from "styled-components";

export const Body = styled.div`
    display: flex;
    width:100vw;
    flex-direction: row;
    text-align: center;
`;
export const Content = styled.div<{isOpen: boolean}>`
    display: flex;
    ${props=>
        !props.isOpen && 
        `width: calc(100vw - 8rem);`
    }
    ${props=>
        props.isOpen && 
        `width: calc(100vw - 16rem);`
    }
    
    flex-direction: row;
    text-align: center;
    background-color: #f0f2f5;
    padding-left: 5vw;
    padding-top: 5vw;
    padding-right: 5vw;
    padding-bottom: 5vw;
`;
export const Box = styled.div`
    width: 100%;
    padding: 0;
    margin: 0;
`;
export const UpperBox = styled.div`

    display: flex;
    flex-direction: column;
    width: 100%;
    height: auto;

    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    background-color: white;

    user-select: none; /* 텍스트 선택 방지 */
    -webkit-user-select: none; /* Safari용 접두사 */
    -moz-user-select: none; /* Firefox용 접두사 */
    -ms-user-select: none; /* Internet Explorer용 접두사 */
`;

export const UnderBox = styled.div`
    width:100%;
    background-color: #f0f2f5;
`
export const Side = styled.div<{ isOpen: boolean}>`
    display: flex;
    flex-direction: column;
    
    height: 100vh;

    background-color: white;
    overflow-x: hidden;
    overflow-y: hidden;
    margin: 0;
`;
// 천천히 효과
export const SideBar = styled.div<{ isOpen: boolean}>`
    
    
    height: 100vh;
    background-color: white;
    ${props =>
        !props.isOpen &&
        `
        animation-duration: 0.7s;
        animation-name: sliden_mini;
        width:5%;
        `
    }
    ${props =>
        props.isOpen &&
        `
        animation-duration: 0.7s;
        animation-name: sliden_max;
        width:10%;
        `
    }

    @keyframes sliden_mini {
        from {
            width: 10%;
        }
        to {
            width: 5%;
        }
    }
    @keyframes sliden_max {
        from {
            width: 5%;
        }
        to {
            width: 10%;
        }
    }
`;
export const Icon = styled.div<{ isOpen: boolean}>`

    display: flex;
    flex-direction: row;
    font-size: 1.5rem;
    
    border: 0;
    background-color: white;
    cursor: pointer;
    &:hover {
        background-color: rgba(0,0,0,0.1);
    }
    ${props=>
        !props.isOpen&&
        `
            width: 1.5rem;
            padding-left: 1rem;
            padding-right: 1rem;
            padding-top: 1rem;
            padding-bottom: 0.5rem;
        `
    }
    ${props=>
        props.isOpen&&
        `
            width: 14rem;
            padding-left: 1rem;
            padding-right: 1rem;
            padding-top: 1rem;
            padding-bottom: 0.5rem; 
        `
    }
`;
export const Name = styled.span`
    padding-left: 2rem;
    font-size: 1rem;
`;