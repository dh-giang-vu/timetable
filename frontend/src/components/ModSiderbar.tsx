import Drawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import InboxIcon from "@mui/icons-material/MoveToInbox";
// import MailIcon from "@mui/icons-material/Mail";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";

import GanttChart from "./GanttChart";
import { TimetableSolution } from "../scripts/api";

interface SidebarProps {
  marginTop: number;
  width: number;
}
const drawerWidth = 240;

/**
 * Renders a sidebar component with navigation links to Campus, Building, Room,
 * Course, and Unit.
 *
 * @param {SidebarProps} props - The properties passed to Sidebar component
 * @param {number} props.marginTop - The top margin of the sidebar.
 * @param {number} props.width - The width of the sidebar.
 * @returns Sidebar component with navigation links.
 */
export default function ModSidebar({ marginTop, width }: SidebarProps) {
  let campusSolutions: TimetableSolution[];
  let campusSolutionsStr: string|null = sessionStorage.getItem("campusSolutions");
  if (campusSolutionsStr !== null) {
    campusSolutions = JSON.parse(campusSolutionsStr);
  } else {
    throw new Error("campusSolutionStr is NULL in ModSidebar")
  }
  return (
    <>
      <Drawer
        sx={{
          width: width,
          flexShrink: 0,
          "& .MuiDrawer-paper": {
            width: drawerWidth,
            boxSizing: "border-box",
            marginTop: marginTop,
          },
        }}
        variant="permanent"
        anchor="left"
      >
        <List>
          {campusSolutions && campusSolutions.length > 0 ? (
            campusSolutions.map((solution) => {
              let campusName = solution.campusName; // Assuming campusName comes from solution
              return (
                <ListItem key={campusName} disablePadding>
                  <ListItemButton
                    component={Link}
                    to={`/timetablemod/${campusName.toLowerCase()}`}
                  >
                    <ListItemIcon>
                      <InboxIcon />
                    </ListItemIcon>
                    <ListItemText primary={campusName} />
                  </ListItemButton>
                </ListItem>
              );
            })
          ) : (
            <div>Loading...</div> // Fallback loading state
          )}
        </List>
      </Drawer>

      <div style={{ marginLeft: drawerWidth }}>
        <Routes>
          {campusSolutions &&
            campusSolutions.length > 0 &&
            campusSolutions.map((solution) => {
              const campusName = solution.campusName;
              return (
                <Route
                  key={campusName}
                  path={`timetablemod/${campusName.toLowerCase()}`}
                  element={<GanttChart/>}
                />
              );
            })}
        </Routes>
      </div>
    </>
  );
}
