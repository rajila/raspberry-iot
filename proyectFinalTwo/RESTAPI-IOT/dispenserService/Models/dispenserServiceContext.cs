using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;

namespace dispenserService.Models
{
    public class dispenserServiceContext : DbContext
    {
        // You can add custom code to this file. Changes will not be overwritten.
        // 
        // If you want Entity Framework to drop and regenerate your database
        // automatically whenever you change your model schema, please use data migrations.
        // For more information refer to the documentation:
        // http://msdn.microsoft.com/en-us/data/jj591621.aspx
    
        public dispenserServiceContext() : base("name=dispenserServiceContext")
        {
        }

        public System.Data.Entity.DbSet<dispenserService.Models.Configuration> Configurations { get; set; }

        public System.Data.Entity.DbSet<dispenserService.Models.FoodDispenser> FoodDispensers { get; set; }

        public System.Data.Entity.DbSet<dispenserService.Models.FoodHour> FoodHours { get; set; }

        public System.Data.Entity.DbSet<dispenserService.Models.Log> Logs { get; set; }

        public System.Data.Entity.DbSet<dispenserService.Models.WaterDispenser> WaterDispensers { get; set; }
    }
}
