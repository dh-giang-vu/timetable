package org.acme;

import ai.timefold.solver.core.api.solver.SolverManager;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.domain.Room;
import org.acme.domain.Student;
import org.acme.domain.Timetable;
import org.acme.domain.Unit;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Entry to the timetabling program.
 * Receives a timetabling problem and outputs the solution
 * with the best optimised scores according to the provided constraints.
 *
 * @author Jet Edge
 */
@Path("/timetabling")
public class TimetableResource {

    @Inject
    SolverManager<Timetable, String> solverManager;

    private int jobId = 0;

    @POST
    @Transactional
    public Timetable handleRequest(Timetable problem) throws ExecutionException, InterruptedException {
        jobId += 1;
        String name = "Job" + Integer.toString(jobId);

        // return problem;

        Timetable solution = solverManager.solve(name, problem).getFinalBestSolution();
        solution.persist();
        return solution;
    }

    @Path("/view")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Timetable> view() {
        return Timetable.listAll();
    }

    @Path("/unit")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Unit handleUnit(Unit unit) {
        return unit;
    }

    @GET
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Timetable solveExample() throws ExecutionException, InterruptedException {

        Student a = new Student("a");
        Student b = new Student("b");
        Student c = new Student("c");
        Student d = new Student("d");
        Student e = new Student("e");
        Student f = new Student("f");
        Student g = new Student("g");
        Student h = new Student("h");
        Student i = new Student("i");

        Room r1 = new Room("Room1", 2, true);
        Room r2 = new Room("Room2", 4, false);
        Room r3 = new Room("Room3", 4, false);

        Unit u1 = new Unit(1, "1", Duration.ofHours(2), List.of(a, b), true);
        Unit u2 = new Unit(2, "2", Duration.ofHours(2), List.of(a, c, d, e), true);
        Unit u3 = new Unit(3, "3", Duration.ofHours(2), List.of(f, g, h, i), false);
        Unit u4 = new Unit(4, "4", Duration.ofHours(2), List.of(a, b), false);

        var problem = new Timetable(
                List.of(
                        u1, u2, u3, u4
//                        new Unit(5, "5", Duration.ofHours(2), List.of(c, d, e)),
//                        new Unit(6, "6", Duration.ofHours(2), List.of(f, g, h, i))
                ),

                List.of(
                        DayOfWeek.MONDAY,
                        DayOfWeek.TUESDAY,
                        DayOfWeek.WEDNESDAY
//                        DayOfWeek.THURSDAY,
//                        DayOfWeek.FRIDAY
                ),

                List.of(
                        LocalTime.of(15, 0)
//                        LocalTime.of(17, 0)
//                        LocalTime.of(16,0),
//                        LocalTime.of(23,0)
                ),
                List.of(r1, r2, r3)
        );

        /*
         * During this solving phase, new Unit objects will be created with the
         * alloted date and Room assignment.
         * 
         * Currently, the 'old' Unit objects in the 'problem' variable and the
         * 'new' Unit objects in the 'solution' variable are stored as different
         * Units in the database due to our inability to control the behaviour
         * of solverManager.solve
         * 
         * i.e. after solving, there will be 2 copies of each Unit in the
         * database, where the 'old' Unit has the list of students but no 
         * timetable assignment, while the 'new' Unit does not have the list 
         * of students enrolled, but does have the assigned date and room
         */
        Timetable solution = solverManager.solve("job 1", problem).getFinalBestSolution();

        solution.persist();     
        // saves the solution timetable and all related entities to database

        return solution;
    }

}