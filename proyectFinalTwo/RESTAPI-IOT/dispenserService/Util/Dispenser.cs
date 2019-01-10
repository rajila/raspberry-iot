using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using dispenserService.Models;

namespace dispenserService.Util
{
    public class Dispenser
    {
        public List<ConfigurationParameter> ListConfigurationParameter { get; set; }

        public List<FoodDispenser> ListFoodDispenser { get; set; }

        public List<WaterDispenser> ListWaterDispenser { get; set; }

        public List<Log> ListLastLog { get; set; }
    }
}