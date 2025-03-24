// import React, { useState, useEffect, useRef } from 'react';
// import { useParams } from 'react-router-dom';
// import axios from 'axios';
// import { Card, CardHeader, CardContent } from '@/components/ui/card';
// import { Button } from '@/components/ui/button';
// import { Alert, AlertDescription } from '@/components/ui/alert';
// import { Play, Square } from 'lucide-react';

// const SimulationDashboard = () => {
//   const { id } = useParams(); // Extract the configuration ID from the URL
//   const [config, setConfig] = useState(null); // Store configuration data
//   const [isRunning, setIsRunning] = useState(false); // Track if the simulation is running
//   const [logs, setLogs] = useState([]); // Store log messages from the simulation
//   const [error, setError] = useState(''); // Store error messages
//   const wsRef = useRef(null); // WebSocket reference for the live logs
//   const logsEndRef = useRef(null); // Reference to scroll to the bottom of the log container

//   useEffect(() => {
//     fetchConfiguration();

//     return () => {
//       if (wsRef.current) {
//         wsRef.current.close();
//       }
//     };
//   }, [id]);

//   // Fetch the configuration data from the server
//   const fetchConfiguration = async () => {
//     try {
//       const response = await axios.get(`/api/configs/get?id=${id}`);
//       setConfig(response.data);
//     } catch (err) {
//       setError('Failed to fetch configuration');
//     }
//   };

//   // Handle WebSocket connections to stream live logs
//   const connectWebSocket = () => {
//     wsRef.current = new WebSocket(`ws://localhost:8080/simulation/${id}`);

//     wsRef.current.onmessage = (event) => {
//       setLogs((prevLogs) => [...prevLogs, event.data]); // Add new logs to the existing logs
//     };

//     wsRef.current.onerror = () => {
//       setError('WebSocket connection error');
//       setIsRunning(false);
//     };

//     wsRef.current.onclose = () => {
//       setIsRunning(false);
//     };
//   };

//   // Scroll to the bottom of the logs container
//   const scrollToBottom = () => {
//     logsEndRef.current?.scrollIntoView({ behavior: 'smooth' });
//   };

//   useEffect(() => {
//     scrollToBottom();
//   }, [logs]);

//   // Start the simulation
//   const handleStart = async () => {
//     try {
//       await axios.post(`/api/simulation/start/${id}`);
//       setIsRunning(true);
//       connectWebSocket();
//     } catch (err) {
//       setError('Failed to start simulation');
//     }
//   };

//   // Stop the simulation
//   const handleStop = async () => {
//     try {
//       await axios.post(`/api/simulation/stop/${id}`);
//       setIsRunning(false);
//       if (wsRef.current) {
//         wsRef.current.close();
//       }
//     } catch (err) {
//       setError('Failed to stop simulation');
//     }
//   };

//   if (!config) {
//     return <div>Loading...</div>;
//   }

//   return (
//     <div>
//       <h1>Simulation Dashboard</h1>
//       {error && (
//         <Alert variant="destructive">
//           <AlertDescription>{error}</AlertDescription>
//         </Alert>
//       )}
//       <Card>
//         <CardHeader>
//           <h2>Configuration #{config.id}</h2>
//         </CardHeader>
//         <CardContent>
//           <p>Number of Vendors: {config.numberOfVendors}</p>
//           <p>Total Tickets: {config.totalTickets}</p>
//           {/* Display other configuration details... */}
//         </CardContent>
//       </Card>

//       <Card>
//         <CardHeader>
//           <Button onClick={handleStart} disabled={isRunning}>
//             <Play /> Start
//           </Button>
//           <Button onClick={handleStop} disabled={!isRunning}>
//             <Square /> Stop
//           </Button>
//         </CardHeader>
//         <CardContent>
//           <div className="log-container">
//             {logs.map((log, index) => (
//               <div key={index}>{log}</div>
//             ))}
//             <div ref={logsEndRef} />
//           </div>
//         </CardContent>
//       </Card>
//     </div>
//   );
// };

// export default SimulationDashboard;
