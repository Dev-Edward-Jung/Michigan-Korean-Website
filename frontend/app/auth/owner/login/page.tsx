// we dont need server infomation
'use client';

import { useState } from 'react';


export default function OwnerLoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [rememberMe, setRememberMe] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            // CSRF 토큰 먼저 가져오기
            const csrfRes = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/csrf`, {
                credentials: 'include',
            });

            if (!csrfRes.ok) throw new Error('CSRF fetch failed');
            console.log(csrfRes)
            const csrf = await csrfRes.json();
            console.log("csrf complete")
            // 로그인 요청
            const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/login/owner`, {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    [csrf.headerName]: csrf.token,
                },
                body: new URLSearchParams({
                    ownerEmail: email,
                    ownerPassword: password,
                    'remember-me': rememberMe ? 'on' : '',
                }),
            });
            console.log(response)
            if (response.ok) {
                // 로그인 성공 시 직접 리다이렉트 (Spring은 HTML로 리턴)
                window.location.href = '/restaurant/list';
            } else {
                const text = await response.text(); // 실패 시 응답 HTML
                console.error('Login failed:', text);
                alert('❌ Login failed');
            }
        } catch (err) {
            console.error('Login error:', err);
            alert('Login error: ' + (err as Error).message);
        }
    };

    return (
        <div className="container-xxl">
            <div className="authentication-wrapper authentication-basic container-p-y">
                <div className="authentication-inner">
                    <div className="card">
                        <div className="card-body">
                            <div className="app-brand justify-content-center">
                                <a href="/page/owner/login" className="app-brand-link gap-2">
                                    <img src="/img/logo/logo-gray.png" className="logo-auth" alt="Logo" />
                                </a>
                            </div>

                            <h4 className="mb-2"><strong>Restaurant Owner Login</strong></h4>
                            <p className="mb-4">Please sign in to your account</p>

                            <form onSubmit={handleSubmit} className="mb-3">
                                <div className="mb-3">
                                    <label htmlFor="email" className="form-label">Email</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="email"
                                        name="ownerEmail"
                                        placeholder="Enter your email"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                    />
                                </div>

                                <div className="mb-3 form-password-toggle">
                                    <div className="d-flex justify-content-between">
                                        <label className="form-label" htmlFor="password">Password</label>
                                        <a href="/page/owner/forgot/password"><small>Forgot Password?</small></a>
                                    </div>
                                    <div className="input-group input-group-merge">
                                        <input
                                            type="password"
                                            id="password"
                                            className="form-control"
                                            name="ownerPassword"
                                            placeholder="••••••••••"
                                            value={password}
                                            onChange={(e) => setPassword(e.target.value)}
                                        />
                                        <span className="input-group-text cursor-pointer">
                      <i className="bx bx-hide"></i>
                    </span>
                                    </div>
                                </div>

                                <div className="mb-3">
                                    <div className="form-check">
                                        <input
                                            className="form-check-input"
                                            type="checkbox"
                                            id="remember-me"
                                            name="remember-me"
                                            checked={rememberMe}
                                            onChange={(e) => setRememberMe(e.target.checked)}
                                        />
                                        <label className="form-check-label" htmlFor="remember-me"> Remember Me </label>
                                    </div>
                                </div>

                                <div className="mb-3">
                                    <button className="btn btn-primary d-grid w-100" type="submit">Sign in</button>
                                </div>
                            </form>

                            <p className="text-center">
                                <span>New on our platform?</span>
                                <a href="/page/owner/register"><span> Create an account</span></a>
                            </p>
                            <p className="text-center">
                                <span>Are you not owner of a restaurant?</span>
                                <a href="/page/employee/login"><span> Employee Login</span></a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}