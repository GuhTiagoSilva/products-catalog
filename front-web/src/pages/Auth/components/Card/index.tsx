import React, { ReactNode } from 'react';
import './styles.scss';

type Props = {
    title: string;
    children: ReactNode;
}

const AuthCard = ({ title, children }: Props) => {
    return (
        <div className="auth-card card-base">
            <h1 className="auth-card-title">
                {title}
            </h1>
            {children}
        </div>
    );
}

export default AuthCard;