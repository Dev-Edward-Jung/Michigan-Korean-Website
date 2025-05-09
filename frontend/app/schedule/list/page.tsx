'use client';

import { useEffect, useState } from 'react';
import { useSearchParams } from 'next/navigation';

export default function EmployeeSchedulePage() {
    const searchParams = useSearchParams();
    const restaurantId = searchParams.get('restaurantId');

    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [kitchenList, setKitchenList] = useState([]);
    const [serverList, setServerList] = useState([]);

    const colorMap = {
        FULL_TIME: 'bg-label-primary',
        DINNER: 'bg-label-info',
        LUNCH: 'bg-label-success',
        OFF: 'bg-label-warning',
    };

    useEffect(() => {
        async function fetchSchedule() {
            if (!restaurantId) return;
            const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

            const res = await fetch(`/api/employee/schedule/list?restaurantId=${restaurantId}`, {
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader || '']: csrfToken || '',
                },
                credentials: 'include',
            });

            if (!res.ok) return alert('Failed to load employee schedules');

            const data = await res.json();
            setKitchenList(data.kitchenList || []);
            setServerList(data.serverList || []);

            const first = data.kitchenList?.[0]?.schedules?.[0];
            if (first) {
                setStartDate(first.shiftStartDate);
                setEndDate(first.shiftEndDate);
            }
        }

        fetchSchedule();
    }, [restaurantId]);

    const renderRows = (list) => {
        return list.map((employee, idx) => [1, 2].map((week) => {
            const isFirstRow = week === 1;
            const schedule = employee.schedules?.length === 14
                ? employee.schedules.slice((week - 1) * 7, week * 7)
                : employee.schedules?.[week - 1] || [];

            return (
                <tr key={`${employee.id}-${week}`}>
                    {isFirstRow && (
                        <td rowSpan={2}><strong>{employee.name}</strong></td>
                    )}
                    {schedule.length === 7 ? schedule.map((s, i) => (
                        <td key={i}>
                            <select
                                className={`form-select form-select-sm form-no-border ${colorMap[s.shift || 'FULL_TIME']}`}
                                disabled
                                defaultValue={s.shift || 'FULL_TIME'}
                            >
                                <option value="FULL_TIME">Full Time</option>
                                <option value="DINNER">Dinner</option>
                                <option value="LUNCH">Lunch</option>
                                <option value="OFF">Off</option>
                            </select>
                        </td>
                    )) : [...Array(7)].map((_, i) => (
                        <td key={i}>
                            <select
                                className={`form-select form-select-sm form-no-border ${colorMap['FULL_TIME']}`}
                                disabled
                                defaultValue="FULL_TIME"
                            >
                                <option value="FULL_TIME">Full Time</option>
                                <option value="DINNER">Dinner</option>
                                <option value="LUNCH">Lunch</option>
                                <option value="OFF">Off</option>
                            </select>
                        </td>
                    ))}
                </tr>
            );
        }));
    };

    const handleEditClick = () => {
        if (restaurantId) {
            window.location.href = `/page/employee/schedule/edit?restaurantId=${restaurantId}`;
        }
    };

    return (
        <div className="card p-4">
            <h5 className="card-header">Schedule List</h5>

            <div className="card-body">
                <div className="table-responsive text-nowrap text-primary mb-3">
                    <div className="input-group-sm list-view">
            <span className="mt-3">
              Start Date:
              <input type="date" className="form-control" value={startDate} disabled />
            </span>
                        <span className="mt-3 m-lg-3">
              End Date:
              <input type="date" className="form-control" value={endDate} disabled />
            </span>
                    </div>
                </div>

                <div className="table-responsive text-nowrap">
                    <p>Kitchen Schedule</p>
                    <table className="table table-bordered kitchen-schedule">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Monday</th>
                            <th>Tuesday</th>
                            <th>Wednesday</th>
                            <th>Thursday</th>
                            <th>Friday</th>
                            <th>Saturday</th>
                            <th>Sunday</th>
                        </tr>
                        </thead>
                        <tbody>{renderRows(kitchenList)}</tbody>
                    </table>
                </div>

                <div className="table-responsive text-nowrap mt-5">
                    <p>Server Schedule</p>
                    <table className="table table-bordered server-schedule">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Monday</th>
                            <th>Tuesday</th>
                            <th>Wednesday</th>
                            <th>Thursday</th>
                            <th>Friday</th>
                            <th>Saturday</th>
                            <th>Sunday</th>
                        </tr>
                        </thead>
                        <tbody>{renderRows(serverList)}</tbody>
                    </table>
                </div>

                <button type="button" className="btn btn-primary mt-3 owner-manager-only" onClick={handleEditClick}>
                    Edit Schedule
                </button>
            </div>
        </div>
    );
}