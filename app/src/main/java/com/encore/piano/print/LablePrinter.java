package com.encore.piano.print;

import android.app.Activity;
import android.content.Context;

import com.encore.piano.activities.AssignmentPrint;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.model.UnitModel;
import com.encore.piano.util.Alerter;
import com.encore.piano.util.DateTimeUtility;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Kamran on 18-Jan-18.
 */

public class LablePrinter {

    Activity context;
    BlueToothPrinter printer;
    EscPosCommands commands;

    public LablePrinter(Activity ctx) {
        context = ctx;
    }

    public void print(AssignmentModel assignment, ArrayList<UnitModel> units) {

        //printer.sendData(context, "Hello World!");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            printer = new BlueToothPrinter();
            if(!printer.findBT(context))
                return;

            OutputStream outputStream = printer.openBT(context);
            commands = new EscPosCommands(outputStream);

            while (units.size() != 1)
                units.remove(0);

            int index = 1;
            for (UnitModel unit: units) {

                commands.init_printer();
                commands.justification_center();
                commands.select_fontB();
                commands.double_height_width_on();
                commands.print_line("Encore Piano");
                commands.double_height_width_off();
                commands.select_fontA();

                commands.print_qr_code(unit.getSerialNumber());

                commands.justification_left();
                commands.print_line("Assignment #: " + assignment.getAssignmentNumber());
                commands.print_line("Time: " + DateTimeUtility.getCurrentTimeStamp());

                commands.print_line("Client: " + assignment.getOrderType() + "  " + assignment.getCustomerCode() + " - " + assignment.getCustomerName());

                commands.justification_center();
                commands.emphasized_on();
                commands.print_line("Pickup Details");
                commands.emphasized_off();
                commands.justification_left();

                commands.print_line("Name: " + assignment.getPickupName());
                commands.print_line("Contact: " + assignment.getPickupPhoneNumber());
                commands.print_line("Address: " + assignment.getPickupAddress());
                commands.print_line("Instructions: " + assignment.getPickupInstructions());

                commands.justification_center();
                commands.emphasized_on();
                commands.print_line("Delivery Details");
                commands.emphasized_off();
                commands.justification_left();

                commands.print_line("Name: " + assignment.getDeliveryName());
                commands.print_line("Contact: " + assignment.getDeliveryPhoneNumber());
                commands.print_line("Address: " + assignment.getDeliveryAddress());
                commands.print_line("Instructions: " + assignment.getDeliveryInstructions());

                commands.justification_center();
                commands.emphasized_on();
                commands.print_line("Unit #: " + index);
                commands.emphasized_off();
                commands.justification_left();

                commands.print_line("Make:      " + unit.getMake() + "  Model: " + unit.getModel());
                commands.print_line("Serial#:   " + unit.getSerialNumber() + "  Benches: " + (unit.isBench() ? "W/B" : "N/B"));
                commands.print_linefeed();

                commands.feedpapercut();

                index++;
            }

            printer.closeBT(context);

        } catch (IOException ex) {
            Alerter.error(context, ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            Alerter.error(context, ex.getMessage());
            ex.printStackTrace();
        }

    }
}
