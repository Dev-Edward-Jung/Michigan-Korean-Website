// frontend/app/restaurant/page.tsx
'use client';

import { useEffect, useState } from 'react';
import api from '@/services/api';

interface Restaurant {
    id: number;
    name: string;
    city: string;
}

export default function RestaurantPage() {
    const [restaurants, setRestaurants] = useState<Restaurant[]>([]);

    useEffect(() => {
        api.get<Restaurant[]>('/api/restaurant/list')
            .then(res => setRestaurants(res.data))
            .catch(err => console.error(err));
    }, []);

    return (
        <main className="p-6">
            <h1 className="text-2xl font-bold">Restaurant List</h1>
            <ul>
                {restaurants.map(r => (
                    <li key={r.id}>{r.name} - {r.city}</li>
                ))}
            </ul>
        </main>
    );
}