namespace dispensatorService.Migrations
{
    using dispensatorService.Models;
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Migrations;
    using System.Linq;

    internal sealed class Configuration : DbMigrationsConfiguration<dispensatorService.Models.dispensatorServiceContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = false;
        }

        protected override void Seed(dispensatorService.Models.dispensatorServiceContext context)
        {
            //  This method will be called after migrating to the latest version.

            //  You can use the DbSet<T>.AddOrUpdate() helper extension method 
            //  to avoid creating duplicate seed data. E.g.
            //
            //    context.People.AddOrUpdate(
            //      p => p.FullName,
            //      new Person { FullName = "Andrew Peters" },
            //      new Person { FullName = "Brice Lambson" },
            //      new Person { FullName = "Rowan Miller" }
            //    );
            //

            //context.Contacts.AddOrUpdate(p => p.Name,
            //      new Contacts
            //      {
            //          ContactId = 1,
            //          Name = "Jessica Diaz",
            //          Address = "Ctra. Valencia km 7",
            //          City = "Madrid",
            //          State = "Spain",
            //          Zip = "28555",
            //          Email = "jdiaz@etsisi.upm.es",
            //      },
            //      new Contacts
            //      {
            //          ContactId = 2,
            //          Name = "Jennifer Perez",
            //          Address = "Ctra. Valencia km 7",
            //          City = "Madrid",
            //          State = "Spain",
            //          Zip = "28555",
            //          Email = "jperez@etsisi.upm.es",
            //      },
            //      new Contacts
            //      {
            //          ContactId = 1,
            //          Name = "Agustin Yague",
            //          Address = "Ctra. Valencia km 7",
            //          City = "Madrid",
            //          State = "Spain",
            //          Zip = "28555",
            //          Email = "ayague@etsisi.upm.es",
            //      }

            //);
        }
    }
}
